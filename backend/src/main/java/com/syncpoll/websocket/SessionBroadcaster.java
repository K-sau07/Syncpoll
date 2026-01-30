package com.syncpoll.websocket;

import com.syncpoll.model.dto.response.ParticipantResponse;
import com.syncpoll.model.dto.response.PollResponse;
import com.syncpoll.model.dto.response.PollResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Handles broadcasting events to WebSocket subscribers.
 * Each session has its own topic: /topic/session/{sessionId}
 */
@Service
public class SessionBroadcaster {

    private static final Logger log = LoggerFactory.getLogger(SessionBroadcaster.class);
    private static final String SESSION_TOPIC = "/topic/session/";

    private final SimpMessagingTemplate messagingTemplate;

    public SessionBroadcaster(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void participantJoined(Long sessionId, ParticipantResponse participant) {
        broadcast(sessionId, EventType.PARTICIPANT_JOINED, participant);
        log.debug("Broadcast: participant {} joined session {}", participant.getDisplayName(), sessionId);
    }

    public void participantLeft(Long sessionId, Long participantId, String displayName) {
        var payload = new ParticipantLeftPayload(participantId, displayName);
        broadcast(sessionId, EventType.PARTICIPANT_LEFT, payload);
        log.debug("Broadcast: participant {} left session {}", displayName, sessionId);
    }

    public void pollStarted(Long sessionId, PollResponse poll) {
        broadcast(sessionId, EventType.POLL_STARTED, poll);
        log.debug("Broadcast: poll {} started in session {}", poll.getId(), sessionId);
    }

    public void pollClosed(Long sessionId, PollResponse poll) {
        broadcast(sessionId, EventType.POLL_CLOSED, poll);
        log.debug("Broadcast: poll {} closed in session {}", poll.getId(), sessionId);
    }

    public void pollResultsUpdated(Long sessionId, PollResultResponse results) {
        broadcast(sessionId, EventType.POLL_RESULTS_UPDATED, results);
        log.debug("Broadcast: poll {} results updated in session {}", results.getPollId(), sessionId);
    }

    public void sessionEnded(Long sessionId) {
        broadcast(sessionId, EventType.SESSION_ENDED, null);
        log.debug("Broadcast: session {} ended", sessionId);
    }

    private <T> void broadcast(Long sessionId, EventType type, T payload) {
        String destination = SESSION_TOPIC + sessionId;
        WebSocketMessage<T> message = WebSocketMessage.of(type, payload);
        messagingTemplate.convertAndSend(destination, message);
    }

    // simple payload for participant left event
    public record ParticipantLeftPayload(Long participantId, String displayName) {}
}
