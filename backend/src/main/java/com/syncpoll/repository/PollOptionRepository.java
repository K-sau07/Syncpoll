package com.syncpoll.repository;

import com.syncpoll.model.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

    List<PollOption> findByPollIdOrderByOptionOrderAsc(Long pollId);

    @Query("SELECT COUNT(po) FROM PollOption po WHERE po.poll.id = :pollId")
    long countByPollId(@Param("pollId") Long pollId);

    void deleteByPollId(Long pollId);
}
