package com.evanw.datebyrate.repository;

import com.evanw.datebyrate.models.RefreshToken;
import com.evanw.datebyrate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

    Boolean existsByToken(String token);
}
