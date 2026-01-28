package com.syncpoll.service;

import com.syncpoll.model.dto.request.SubmitAnswerRequest;
import com.syncpoll.model.entity.*;
import com.syncpoll.repository.AnswerRepository;
import com.syncpoll.repository.AttendanceRepository;
import com.syncpoll.repository.PollOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AnswerService {

    private static final Logger log = LoggerFactory.getLogger(AnswerService.class);

    private final AnswerRepository answerRepository;
    private final PollOptionRepository pollOptionRepository;
    private final AttendanceRepository attendanceRepository;
    private final PollService pollService;
    private final ParticipantService participantService;

    public AnswerService(AnswerRepository answerRepository,
                         PollOptionRepository pollOptionRepository,
                         AttendanceRepository attendanceRepository,
                         PollService pollService,
                         ParticipantService participantService) {
        this.answerRepository = answerRepository;
        this.pollOptionRepository = pollOptionRepository;
        this.attendanceRepository = attendanceRepository;
        this.pollService = pollService;
        this.participantService = participantService;
    }

    @Transactional
    public void submitAnswer(Long pollId, Long participantId, SubmitAnswerRequest request) {
        Poll poll = pollService.getPollEntity(pollId);

        if (!poll.acceptingAnswers()) {
            throw new IllegalStateException("This poll is not accepting answers");
        }

        Participant participant = participantService.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));

        // make sure participant belongs to this session
        if (!participant.getSession().getId().equals(poll.getSession().getId())) {
            throw new IllegalArgumentException("Participant does not belong to this session");
        }

        // check if already answered
        if (answerRepository.existsByPollAndParticipant(poll, participant)) {
            throw new IllegalStateException("You have already answered this poll");
        }

        PollOption selectedOption = pollOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option"));

        // make sure option belongs to this poll
        if (!selectedOption.getPoll().getId().equals(pollId)) {
            throw new IllegalArgumentException("Option does not belong to this poll");
        }

        Answer answer = new Answer(poll, participant, selectedOption);
        answerRepository.save(answer);

        // update attendance count
        Optional<Attendance> attendance = attendanceRepository.findByParticipant(participant);
        attendance.ifPresent(a -> {
            a.incrementAnswerCount();
            attendanceRepository.save(a);
        });

        log.debug("Participant {} answered poll {} with option {}", participantId, pollId, request.getOptionId());
    }

    public boolean hasAnswered(Long pollId, Long participantId) {
        return answerRepository.existsByPollIdAndParticipantId(pollId, participantId);
    }
}
