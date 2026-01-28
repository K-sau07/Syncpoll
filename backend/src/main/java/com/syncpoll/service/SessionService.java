package com.syncpoll.service;

import com.syncpoll.exception.SessionNotFoundException;
import com.syncpoll.exception.UnauthorizedException;
import com.syncpoll.model.dto.request.CreateSessionRequest;
import com.syncpoll.model.dto.response.SessionResponse;
import com.syncpoll.model.entity.Session;
import com.syncpoll.model.entity.SessionStatus;
import com.syncpoll.model.entity.User;
import com.syncpoll.repository.SessionRepository;
import com.syncpoll.util.JoinCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request, User host) {
        String joinCode = generateUniqueJoinCode();

        Session session = new Session(host, request.getTitle(), joinCode);
        session.setDescription(request.getDescription());
        session.setJoinMode(request.getJoinMode());

        Session saved = sessionRepository.save(session);
        log.info("Created session '{}' with join code {} for host {}", saved.getTitle(), joinCode, host.getEmail());

        return SessionResponse.fromEntity(saved);
    }

    public SessionResponse getSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
        return SessionResponse.fromEntity(session);
    }

    public SessionResponse getSessionByJoinCode(String joinCode) {
        Session session = sessionRepository.findByJoinCode(joinCode.toUpperCase())
                .orElseThrow(() -> new SessionNotFoundException("joinCode", joinCode));
        return SessionResponse.fromEntity(session);
    }

    public Session getSessionEntity(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    public Session getSessionEntityByJoinCode(String joinCode) {
        return sessionRepository.findByJoinCode(joinCode.toUpperCase())
                .orElseThrow(() -> new SessionNotFoundException("joinCode", joinCode));
    }

    public List<SessionResponse> getSessionsByHost(User host) {
        return sessionRepository.findByHostOrderByCreatedAtDesc(host).stream()
                .map(SessionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<SessionResponse> getSessionsByHost(User host, Pageable pageable) {
        return sessionRepository.findByHostOrderByCreatedAtDesc(host, pageable)
                .map(SessionResponse::fromEntity);
    }

    public List<SessionResponse> getActiveSessionsByHost(User host) {
        return sessionRepository.findByHostAndStatus(host, SessionStatus.ACTIVE).stream()
                .map(SessionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public SessionResponse endSession(Long sessionId, User host) {
        Session session = getSessionEntity(sessionId);
        
        if (!session.getHost().getId().equals(host.getId())) {
            throw new UnauthorizedException("Only the host can end this session");
        }

        session.end();
        Session saved = sessionRepository.save(session);
        log.info("Session {} ended by host {}", sessionId, host.getEmail());

        return SessionResponse.fromEntity(saved);
    }

    @Transactional
    public void deleteSession(Long sessionId, User host) {
        Session session = getSessionEntity(sessionId);
        
        if (!session.getHost().getId().equals(host.getId())) {
            throw new UnauthorizedException("Only the host can delete this session");
        }

        sessionRepository.delete(session);
        log.info("Session {} deleted by host {}", sessionId, host.getEmail());
    }

    public boolean isHost(Long sessionId, User user) {
        Session session = getSessionEntity(sessionId);
        return session.getHost().getId().equals(user.getId());
    }

    private String generateUniqueJoinCode() {
        String code;
        int attempts = 0;
        
        do {
            code = JoinCodeGenerator.generate();
            attempts++;
            if (attempts > 10) {
                throw new RuntimeException("Failed to generate unique join code after 10 attempts");
            }
        } while (sessionRepository.existsByJoinCode(code));

        return code;
    }
}
