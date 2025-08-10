package rate.service;

import org.springframework.stereotype.Service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UserRateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private static final int USER_LIMIT = 3; // per window
    private static final Duration WINDOW = Duration.ofSeconds(10);

    public UserRateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String userId) {
        String key = "rate:user:" + userId;

        // Increment and get new count
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            // First request, set expiry
            redisTemplate.expire(key, WINDOW);
        }

        return count != null && count <= USER_LIMIT;
    }
}
