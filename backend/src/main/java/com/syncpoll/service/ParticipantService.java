package com.syncpoll.service;

import com.syncpoll.model.dto.response.ParticipantResponse;
import com.syncpoll.model.entity.*;
import com.syncpoll.repository.AttendanceRepository;
import com.syncpoll.repository.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    private static final Logger log = LoggerFactory.getLogger(ParticipantService.class);

    private final ParticipantRepository participantRepository;
    private final AttendanceRepository attendanceRepository;
    private final SessionService sessionService;

    public ParticipantService(ParticipantRepository participantRepository,
                              AttendanceRepository attendanceRepository,
                              SessionService sessionService) {
        this.participantRepository = participantRepository;
        this.attendanceRepository = attendanceRepository;
        this.sessionService = sessionService;
    }

    @Transactional
    public ParticipantResponse joinSession(String joinCode, String displayName) {
        Session session = sessionService.getSessionEntityByJoinCode(joinCode);

        if (!session.isActive()) {
            throw new IllegalStateException("This session is no longer active");
        }

        if (session.getJoinMode() == JoinMode.VERIFIED) {
            throw new IllegalStateException("This session requires Google login to join");
        }

        // check if someone with this name already joined
        Optional<Participant> existing = participantRepository
                .findBySessionIdAndDisplayName(session.getId(), displayName);
        
        if (existing.isPresent()) {
            log.debug("Participant '{}' rejoining session {}", displayName, session.getId());
            return ParticipantResponse.fromEntity(existing.get());
        }

        Participant participant = new Participant(session, displayName);
        Participant saved = participantRepository.save(participant);

        // create attendance record
        Attendance attendance = new Attendance(session, saved);
        attendanceRepository.save(attendance);

        log.info("Participant '{}' joined session {} (open mode)", displayName, session.getId());
        return ParticipantResponse.fromEntity(saved);
    }

    @Transactional
    public ParticipantResponse joinSessionVerified(String joinCode, User user) {
        Session session = sessionService.getSessionEntityByJoinCode(joinCode);

        if (!session.isActive()) {
            throw new IllegalStateException("This session is no longer active");
        }

        // check if this user already joined
        Optional<Participant> existing = participantRepository
                .findBySessionAndUser(session, user);
        
        if (existing.isPresent()) {
            log.debug("User {} rejoining session {}", user.getEmail(), session.getId());
            return ParticipantResponse.fromEntity(existing.get());
        }

        Participant participant = new Participant(session, user);
        Participant saved = participantRepository.save(participant);

        // create attendance record
        Attendance attendance = new Attendance(session, saved);
        attendanceRepository.save(attendance);

        log.info("User {} joined session {} (verified mode)", user.getEmail(), session.getId());
        return ParticipantResponse.fromEntity(saved);
    }

    public List<ParticipantResponse> getParticipantsBySession(Long sessionId) {
        return participantRepository.findBySessionId(sessionId).stream()
                .map(ParticipantResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<Participant> findById(Long participantId) {
        return participantRepository.findById(participantId);
    }

    public long getParticipantCount(Long sessionId) {
        return participantRepository.countBySessionId(sessionId);
    }
}
