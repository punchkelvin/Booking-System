package com.example.bookingDemo.service;

import com.example.bookingDemo.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class RefreshTokenCleanupService {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

//    @Scheduled(cron = "*/10 * * * * *") //10 seconds
    @Scheduled(cron = "0 0 * * * ?") //1 hour
    @Transactional
    public void deleteExpiredTokens(){
        refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
        log.info("Expired Token Deleted");
    }
}
