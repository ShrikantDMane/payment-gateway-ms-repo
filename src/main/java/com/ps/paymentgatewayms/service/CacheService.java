package com.ps.paymentgatewayms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void cacheUserDetails(String userId, User user) {
        redisTemplate.opsForValue().set(userId, user);
    }

    public User getUserDetails(String userId) {
        return (User) redisTemplate.opsForValue().get(userId);
    }

    public void deleteUserDetails(String userId) {
        redisTemplate.opsForValue().getOperations().delete(userId);
    }
}

