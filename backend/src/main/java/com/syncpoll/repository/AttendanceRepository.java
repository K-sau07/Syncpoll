package com.syncpoll.repository;

import com.syncpoll.model.entity.Attendance;
import com.syncpoll.model.entity.Participant;
import com.syncpoll.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findBySession(Session session);

    List<Attendance> findBySessionId(Long sessionId);

    Optional<Attendance> findBySessionAndParticipant(Session session, Participant participant);

    Optional<Attendance> findByParticipant(Participant participant);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.session.id = :sessionId")
    long countBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT AVG(a.answersCount) FROM Attendance a WHERE a.session.id = :sessionId")
    Double averageAnswersCountBySessionId(@Param("sessionId") Long sessionId);
}
