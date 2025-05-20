package com.example.springboot.controller;

import com.example.springboot.service.RedisLockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LockController {

    private final RedisLockService redisLockService;

    public LockController(RedisLockService redisLockService) {
        this.redisLockService = redisLockService;
    }

    @GetMapping("/lock")
    public String lock() {
        String lockKey = "myLock";
        String requestId = UUID.randomUUID().toString();
        boolean locked = redisLockService.tryLock(lockKey, requestId, 10); // 10秒过期

        if (locked) {
            try {
                // 执行业务逻辑
                return "Lock acquired and business logic executed.";
            } finally {
                // 释放锁
                redisLockService.releaseLock(lockKey, requestId);
            }
        } else {
            return "Failed to acquire lock.";
        }
    }
} 