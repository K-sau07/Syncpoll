package com.syncpoll.repository;

import com.syncpoll.model.entity.Session;
import com.syncpoll.model.entity.SessionStatus;
import com.syncpoll.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByJoinCode(String joinCode);

    boolean existsByJoinCode(String joinCode);

    List<Session> findByHostOrderByCreatedAtDesc(User host);

    Page<Session> findByHostOrderByCreatedAtDesc(User host, Pageable pageable);

    List<Session> findByHostAndStatus(User host, SessionStatus status);

    @Query("SELECT s FROM Session s WHERE s.host.id = :hostId AND s.status = :status")
    List<Session> findByHostIdAndStatus(@Param("hostId") Long hostId, @Param("status") SessionStatus status);

    @Query("SELECT COUNT(s) FROM Session s WHERE s.host.id = :hostId")
    long countByHostId(@Param("hostId") Long hostId);
}
