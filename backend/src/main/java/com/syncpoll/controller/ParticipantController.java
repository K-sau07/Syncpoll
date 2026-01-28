package com.syncpoll.controller;

import com.syncpoll.model.dto.request.JoinSessionRequest;
import com.syncpoll.model.dto.response.ParticipantResponse;
import com.syncpoll.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<ParticipantResponse> joinSession(
            @Valid @RequestBody JoinSessionRequest request) {
        
        ParticipantResponse participant = participantService.joinSession(
                request.getJoinCode(),
                request.getDisplayName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(participant);
    }
}
