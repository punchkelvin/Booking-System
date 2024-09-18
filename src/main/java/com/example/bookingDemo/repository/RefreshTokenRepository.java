package com.example.bookingDemo.repository;

import com.example.bookingDemo.model.RefreshToken;
import com.example.bookingDemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);

    void deleteByUser(User user);

    void deleteAllByExpiryDateBefore(Instant now);
}
