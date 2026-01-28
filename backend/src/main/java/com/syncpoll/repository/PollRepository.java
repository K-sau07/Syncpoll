package com.syncpoll.repository;

import com.syncpoll.model.entity.Poll;
import com.syncpoll.model.entity.PollStatus;
import com.syncpoll.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {

    List<Poll> findBySessionOrderByCreatedAtDesc(Session session);

    List<Poll> findBySessionId(Long sessionId);

    List<Poll> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

    List<Poll> findBySessionAndStatus(Session session, PollStatus status);

    Optional<Poll> findBySessionAndStatus(Session session, PollStatus status, org.springframework.data.domain.Sort sort);

    @Query("SELECT p FROM Poll p WHERE p.session.id = :sessionId AND p.status = :status")
    List<Poll> findBySessionIdAndStatus(@Param("sessionId") Long sessionId, @Param("status") PollStatus status);

    @Query("SELECT p FROM Poll p WHERE p.session.id = :sessionId AND p.status = 'LIVE'")
    Optional<Poll> findActivePollBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT COUNT(p) FROM Poll p WHERE p.session.id = :sessionId")
    long countBySessionId(@Param("sessionId") Long sessionId);
}
