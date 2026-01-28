package com.syncpoll.repository;

import com.syncpoll.model.entity.Participant;
import com.syncpoll.model.entity.Session;
import com.syncpoll.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findBySession(Session session);

    List<Participant> findBySessionId(Long sessionId);

    Optional<Participant> findBySessionAndUser(Session session, User user);

    Optional<Participant> findBySessionIdAndUserId(Long sessionId, Long userId);

    @Query("SELECT p FROM Participant p WHERE p.session.id = :sessionId AND p.displayName = :displayName")
    Optional<Participant> findBySessionIdAndDisplayName(@Param("sessionId") Long sessionId, 
                                                         @Param("displayName") String displayName);

    @Query("SELECT COUNT(p) FROM Participant p WHERE p.session.id = :sessionId")
    long countBySessionId(@Param("sessionId") Long sessionId);

    boolean existsBySessionAndUser(Session session, User user);
}
