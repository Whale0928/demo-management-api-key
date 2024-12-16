package app.demo.management.key.aop;

import app.demo.management.key.jwt.JwtTokenDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiCallCounterAspect {
    private final StringRedisTemplate redisTemplate;
    private final JwtTokenDecoder decoder;

    @Around("@annotation(app.demo.management.key.aop.CountApiCall)")
    public Object countApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        final String apiKey = extractApiKey();
        try {
            String email = decoder.extractEmail(apiKey);
            incrementApiCount(email);
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("API call counting failed: {}", e.getMessage());
            throw e;
        }
    }

    private String extractApiKey() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String apiKey = request.getHeader("X-API-KEY");

        if (apiKey.isBlank())
            throw new IllegalArgumentException("API key is missing");

        return apiKey;
    }

    private void incrementApiCount(String email) {
        String key = String.format("client:%s", email);
        Long newCount = redisTemplate.opsForValue().increment(key);

        log.info("클라이언트 {}의 API 호출 횟수: {}", email, newCount);
    }
}
