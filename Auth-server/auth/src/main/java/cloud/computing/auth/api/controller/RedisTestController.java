package cloud.computing.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisTestController {


    private final StringRedisTemplate redisTemplate;

    @GetMapping("/test-redis")
    public String testRedisConnection() {
        try {
            redisTemplate.opsForValue().set("test", "success");
            String value = redisTemplate.opsForValue().get("test");
            return "Redis 연결 성공: " + value;
        } catch (Exception e) {
            return "Redis 연결 실패: " + e.getMessage();
        }
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Application is running";
    }
}
