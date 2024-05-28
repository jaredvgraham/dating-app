package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.Match;
import com.evanw.datebyrate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query(value = "SELECT * FROM matches m WHERE :userId = m.user_id", nativeQuery = true)
    List<Match> findAllByUser(@Param("userId") Integer userId);

    @Query(value = "SELECT * FROM matches m WHERE :matchKey = m.key AND :userId = m.user_id", nativeQuery = true)
    Optional<Match> findMatchByKey(@Param("matchKey") String matchKey,
                                   @Param("userId") Integer userId);

}
