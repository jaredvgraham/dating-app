package com.evanw.datebyrate.service;

import com.evanw.datebyrate.models.RefreshToken;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.RefreshTokenRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public String createRefreshToken(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpirationDate(Instant.now().plus(365, ChronoUnit.DAYS));

        repository.save(refreshToken);

        return token;
    }
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token);
    }
    public boolean isExpired(RefreshToken refreshToken) {
        if (refreshToken.getExpirationDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session has expired");
            return true;
        }
        return false;
    }
    @Transactional
    public void delete(User user) {
        repository.deleteByUser(user);
    }
}
