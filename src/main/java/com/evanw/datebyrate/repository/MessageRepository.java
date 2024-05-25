package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query(value = "SELECT * FROM messages m WHERE :matchKey = match_key", nativeQuery = true)
    List<Message> findMessageByMatchKey(@Param("matchKey") String matchKey);

    @Query(value = "SELECT * FROM messages m WHERE m.match_key = :matchKey ORDER BY m.timestamp DESC LIMIT 1", nativeQuery = true)
    Optional<Message> findLatestMessage(@Param("matchKey") String matchKey);

    @Modifying
    @Transactional
    @Query(value = "UPDATE messages SET read = TRUE WHERE match_key = :matchKey AND timestamp <= :timestamp AND read = FALSE AND sender_id != :userId", nativeQuery = true)
    void setAllPreviousMessagesRead(@Param("matchKey") String matchKey,
                                          @Param("timestamp")LocalDateTime timestamp,
                                          @Param("userId") Integer userId);

    boolean existsByMatchKey(String matchKey);
}
