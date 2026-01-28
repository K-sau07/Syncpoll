package com.syncpoll.service;

import com.syncpoll.model.dto.response.AttendanceResponse;
import com.syncpoll.model.entity.Attendance;
import com.syncpoll.model.entity.Session;
import com.syncpoll.repository.AttendanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final SessionService sessionService;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             SessionService sessionService) {
        this.attendanceRepository = attendanceRepository;
        this.sessionService = sessionService;
    }

    public List<AttendanceResponse> getAttendanceBySession(Long sessionId) {
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(AttendanceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public long getAttendanceCount(Long sessionId) {
        return attendanceRepository.countBySessionId(sessionId);
    }

    public Double getAverageEngagement(Long sessionId) {
        return attendanceRepository.averageAnswersCountBySessionId(sessionId);
    }

    @Transactional
    public void markParticipantLeft(Long participantId) {
        attendanceRepository.findById(participantId).ifPresent(attendance -> {
            attendance.markLeft();
            attendanceRepository.save(attendance);
            log.debug("Marked participant {} as left", participantId);
        });
    }
}
