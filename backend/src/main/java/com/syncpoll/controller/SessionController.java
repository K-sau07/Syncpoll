package com.syncpoll.controller;

import com.syncpoll.model.dto.request.CreateSessionRequest;
import com.syncpoll.model.dto.response.AttendanceResponse;
import com.syncpoll.model.dto.response.ParticipantResponse;
import com.syncpoll.model.dto.response.SessionResponse;
import com.syncpoll.model.entity.User;
import com.syncpoll.service.AttendanceService;
import com.syncpoll.service.AuthService;
import com.syncpoll.service.ParticipantService;
import com.syncpoll.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final ParticipantService participantService;
    private final AttendanceService attendanceService;
    private final AuthService authService;

    public SessionController(SessionService sessionService,
                             ParticipantService participantService,
                             AttendanceService attendanceService,
                             AuthService authService) {
        this.sessionService = sessionService;
        this.participantService = participantService;
        this.attendanceService = attendanceService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> createSession(
            @Valid @RequestBody CreateSessionRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        // temporary: get or create a test user until OAuth is set up
        User host = getOrCreateTestUser(userId);
        SessionResponse session = sessionService.createSession(request, host);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.getSession(sessionId));
    }

    @GetMapping("/join/{joinCode}")
    public ResponseEntity<SessionResponse> getSessionByJoinCode(@PathVariable String joinCode) {
        return ResponseEntity.ok(sessionService.getSessionByJoinCode(joinCode));
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> getMySessions(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        return ResponseEntity.ok(sessionService.getSessionsByHost(host));
    }

    @PostMapping("/{sessionId}/end")
    public ResponseEntity<SessionResponse> endSession(
            @PathVariable Long sessionId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        return ResponseEntity.ok(sessionService.endSession(sessionId, host));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable Long sessionId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        sessionService.deleteSession(sessionId, host);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable Long sessionId) {
        return ResponseEntity.ok(participantService.getParticipantsBySession(sessionId));
    }

    @GetMapping("/{sessionId}/attendance")
    public ResponseEntity<List<AttendanceResponse>> getAttendance(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getAttendanceBySession(sessionId));
    }

    // temporary helper until OAuth is set up
    private User getOrCreateTestUser(Long userId) {
        if (userId != null) {
            return authService.findById(userId).orElseGet(this::createTestUser);
        }
        return createTestUser();
    }

    private User createTestUser() {
        return authService.findOrCreateUser(
                "test@syncpoll.dev",
                "Test User",
                "test-google-id-123",
                null
        );
    }
}
