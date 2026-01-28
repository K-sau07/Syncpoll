package com.syncpoll.repository;

import com.syncpoll.model.entity.Answer;
import com.syncpoll.model.entity.Participant;
import com.syncpoll.model.entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByPoll(Poll poll);

    List<Answer> findByPollId(Long pollId);

    List<Answer> findByParticipant(Participant participant);

    List<Answer> findByParticipantId(Long participantId);

    Optional<Answer> findByPollAndParticipant(Poll poll, Participant participant);

    Optional<Answer> findByPollIdAndParticipantId(Long pollId, Long participantId);

    boolean existsByPollAndParticipant(Poll poll, Participant participant);

    boolean existsByPollIdAndParticipantId(Long pollId, Long participantId);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.poll.id = :pollId")
    long countByPollId(@Param("pollId") Long pollId);

    @Query("SELECT COUNT(a) FROM Answer a WHERE a.poll.id = :pollId AND a.selectedOption.id = :optionId")
    long countByPollIdAndOptionId(@Param("pollId") Long pollId, @Param("optionId") Long optionId);

    @Query("SELECT a.selectedOption.id, COUNT(a) FROM Answer a WHERE a.poll.id = :pollId GROUP BY a.selectedOption.id")
    List<Object[]> countAnswersByOption(@Param("pollId") Long pollId);
}
