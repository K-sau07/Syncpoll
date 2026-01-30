package com.syncpoll.service;

import com.syncpoll.exception.PollNotFoundException;
import com.syncpoll.exception.UnauthorizedException;
import com.syncpoll.model.dto.request.CreatePollRequest;
import com.syncpoll.model.dto.response.PollResponse;
import com.syncpoll.model.dto.response.PollResultResponse;
import com.syncpoll.model.entity.*;
import com.syncpoll.repository.AnswerRepository;
import com.syncpoll.repository.PollOptionRepository;
import com.syncpoll.repository.PollRepository;
import com.syncpoll.websocket.SessionBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PollService {

    private static final Logger log = LoggerFactory.getLogger(PollService.class);

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final AnswerRepository answerRepository;
    private final SessionService sessionService;
    private final SessionBroadcaster broadcaster;

    public PollService(PollRepository pollRepository, 
                       PollOptionRepository pollOptionRepository,
                       AnswerRepository answerRepository,
                       SessionService sessionService,
                       SessionBroadcaster broadcaster) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.answerRepository = answerRepository;
        this.sessionService = sessionService;
        this.broadcaster = broadcaster;
    }

    @Transactional
    public PollResponse createPoll(Long sessionId, CreatePollRequest request, User host) {
        Session session = sessionService.getSessionEntity(sessionId);

        if (!session.getHost().getId().equals(host.getId())) {
            throw new UnauthorizedException("Only the host can create polls");
        }

        if (!session.isActive()) {
            throw new IllegalStateException("Cannot create poll in an inactive session");
        }

        Poll poll = new Poll(session, request.getQuestion());
        poll.setPollType(request.getPollType());
        poll.setShowResults(request.isShowResults());

        for (int i = 0; i < request.getOptions().size(); i++) {
            poll.addOption(request.getOptions().get(i));
        }

        Poll saved = pollRepository.save(poll);
        log.info("Created poll '{}' in session {}", saved.getQuestion(), sessionId);

        return PollResponse.fromEntity(saved);
    }

    public PollResponse getPoll(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));
        return PollResponse.fromEntity(poll);
    }

    public Poll getPollEntity(Long pollId) {
        return pollRepository.findById(pollId)
                .orElseThrow(() -> new PollNotFoundException(pollId));
    }

    public List<PollResponse> getPollsBySession(Long sessionId) {
        return pollRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(PollResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PollResponse startPoll(Long pollId, User host) {
        Poll poll = getPollEntity(pollId);
        Session session = poll.getSession();

        if (!session.getHost().getId().equals(host.getId())) {
            throw new UnauthorizedException("Only the host can start polls");
        }

        if (poll.getStatus() != PollStatus.DRAFT) {
            throw new IllegalStateException("Poll has already been started");
        }

        // close any currently live poll in this session
        pollRepository.findBySessionIdAndStatus(session.getId(), PollStatus.LIVE)
                .forEach(livePoll -> {
                    livePoll.close();
                    pollRepository.save(livePoll);
                    broadcaster.pollClosed(session.getId(), PollResponse.fromEntity(livePoll));
                });

        poll.goLive();
        Poll saved = pollRepository.save(poll);
        
        PollResponse response = PollResponse.fromEntity(saved);
        broadcaster.pollStarted(session.getId(), response);
        
        log.info("Poll {} is now live in session {}", pollId, session.getId());
        return response;
    }

    @Transactional
    public PollResponse closePoll(Long pollId, User host) {
        Poll poll = getPollEntity(pollId);
        Session session = poll.getSession();

        if (!session.getHost().getId().equals(host.getId())) {
            throw new UnauthorizedException("Only the host can close polls");
        }

        if (poll.getStatus() != PollStatus.LIVE) {
            throw new IllegalStateException("Poll is not currently live");
        }

        poll.close();
        Poll saved = pollRepository.save(poll);
        
        PollResponse response = PollResponse.fromEntity(saved);
        broadcaster.pollClosed(session.getId(), response);
        
        log.info("Poll {} closed in session {}", pollId, session.getId());
        return response;
    }

    public PollResultResponse getPollResults(Long pollId) {
        Poll poll = getPollEntity(pollId);
        return buildPollResults(poll);
    }

    public PollResultResponse buildPollResults(Poll poll) {
        List<Object[]> voteCounts = answerRepository.countAnswersByOption(poll.getId());
        Map<Long, Long> voteMap = voteCounts.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));

        int totalVotes = voteMap.values().stream().mapToInt(Long::intValue).sum();

        List<PollResultResponse.OptionResultResponse> results = new ArrayList<>();
        for (PollOption option : poll.getOptions()) {
            int votes = voteMap.getOrDefault(option.getId(), 0L).intValue();
            double percentage = totalVotes > 0 ? (votes * 100.0 / totalVotes) : 0;
            results.add(new PollResultResponse.OptionResultResponse(
                    option.getId(),
                    option.getOptionText(),
                    votes,
                    Math.round(percentage * 10) / 10.0
            ));
        }

        PollResultResponse response = new PollResultResponse();
        response.setPollId(poll.getId());
        response.setQuestion(poll.getQuestion());
        response.setTotalVotes(totalVotes);
        response.setResults(results);

        return response;
    }

    @Transactional
    public void deletePoll(Long pollId, User host) {
        Poll poll = getPollEntity(pollId);
        Session session = poll.getSession();

        if (!session.getHost().getId().equals(host.getId())) {
            throw new UnauthorizedException("Only the host can delete polls");
        }

        pollRepository.delete(poll);
        log.info("Poll {} deleted from session {}", pollId, session.getId());
    }
}
