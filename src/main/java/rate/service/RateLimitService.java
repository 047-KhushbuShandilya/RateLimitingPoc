package rate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    @Value("${ratelimit.api.limit}")
    private int apiLimit;

    @Value("${ratelimit.api.duration}")
    private int apiDuration;

    @Value("${ratelimit.user.limit}")
    private int userLimit;

    @Value("${ratelimit.user.duration}")
    private int userDuration;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowedForApi(String apiKey) {
        return checkLimit("api:" + apiKey, apiLimit, apiDuration);
    }

    public boolean isAllowedForUser(String userId, String apiKey) {
        return checkLimit("user:" + userId + ":api:" + apiKey, userLimit, userDuration);
    }

    private boolean checkLimit(String key, int limit, int durationSeconds) {
        Long currentCount = redisTemplate.opsForValue().increment(key);
        if (currentCount == 1) {
            redisTemplate.expire(key, durationSeconds, TimeUnit.SECONDS);
        }
        return currentCount <= limit;
    }
}
