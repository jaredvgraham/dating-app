package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(value = "SELECT rate FROM rating WHERE rated_user_id = :ratedUserId", nativeQuery = true)
    List<Integer> findRatingsForUser(@Param("ratedUserId") Integer ratedUserId);
}
