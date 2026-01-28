package com.syncpoll.controller;

import com.syncpoll.model.dto.request.CreatePollRequest;
import com.syncpoll.model.dto.request.SubmitAnswerRequest;
import com.syncpoll.model.dto.response.PollResponse;
import com.syncpoll.model.dto.response.PollResultResponse;
import com.syncpoll.model.entity.User;
import com.syncpoll.service.AnswerService;
import com.syncpoll.service.AuthService;
import com.syncpoll.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/{sessionId}/polls")
public class PollController {

    private final PollService pollService;
    private final AnswerService answerService;
    private final AuthService authService;

    public PollController(PollService pollService, 
                          AnswerService answerService,
                          AuthService authService) {
        this.pollService = pollService;
        this.answerService = answerService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<PollResponse> createPoll(
            @PathVariable Long sessionId,
            @Valid @RequestBody CreatePollRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        PollResponse poll = pollService.createPoll(sessionId, request, host);
        return ResponseEntity.status(HttpStatus.CREATED).body(poll);
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> getPolls(@PathVariable Long sessionId) {
        return ResponseEntity.ok(pollService.getPollsBySession(sessionId));
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<PollResponse> getPoll(
            @PathVariable Long sessionId,
            @PathVariable Long pollId) {
        return ResponseEntity.ok(pollService.getPoll(pollId));
    }

    @PostMapping("/{pollId}/start")
    public ResponseEntity<PollResponse> startPoll(
            @PathVariable Long sessionId,
            @PathVariable Long pollId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        return ResponseEntity.ok(pollService.startPoll(pollId, host));
    }

    @PostMapping("/{pollId}/close")
    public ResponseEntity<PollResponse> closePoll(
            @PathVariable Long sessionId,
            @PathVariable Long pollId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        return ResponseEntity.ok(pollService.closePoll(pollId, host));
    }

    @GetMapping("/{pollId}/results")
    public ResponseEntity<PollResultResponse> getPollResults(
            @PathVariable Long sessionId,
            @PathVariable Long pollId) {
        return ResponseEntity.ok(pollService.getPollResults(pollId));
    }

    @PostMapping("/{pollId}/answer")
    public ResponseEntity<Void> submitAnswer(
            @PathVariable Long sessionId,
            @PathVariable Long pollId,
            @Valid @RequestBody SubmitAnswerRequest request,
            @RequestHeader("X-Participant-Id") Long participantId) {
        
        answerService.submitAnswer(pollId, participantId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(
            @PathVariable Long sessionId,
            @PathVariable Long pollId,
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        
        User host = getOrCreateTestUser(userId);
        pollService.deletePoll(pollId, host);
        return ResponseEntity.noContent().build();
    }

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
