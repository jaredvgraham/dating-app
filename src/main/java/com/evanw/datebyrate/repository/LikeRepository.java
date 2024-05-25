package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.Like;
import com.evanw.datebyrate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByLikerAndLiked(User likerId, User likedId);
}
