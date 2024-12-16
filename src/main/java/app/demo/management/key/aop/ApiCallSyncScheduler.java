package app.demo.management.key.aop;


import app.demo.management.key.client.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiCallSyncScheduler {
    private final StringRedisTemplate redisTemplate;
    private final ClientRepository clientRepository;

    @Transactional
   // @Scheduled(fixedRate = 5000)//10초마다 실행
    public void syncApiCalls() {
        log.info("Starting API call count synchronization");

        try {
            // 모든 클라이언트의 카운트 키 패턴
            Set<String> keys = redisTemplate.keys("client:*:count");

            log.info("redis keys : {}", keys);


            for (String key : keys)
                syncClientApiCalls(key);

        } catch (Exception e) {
            log.error("Failed to sync API calls", e);
        }
    }

    private void syncClientApiCalls(String key) {
        try {
            // client:123:count 형식에서 ID 추출
            Integer clientId = extractClientId(key);
            String countStr = redisTemplate.opsForValue().get(key);

            if (countStr == null) {
                return;
            }

            int count = Integer.parseInt(countStr);

            // DB 업데이트
            clientRepository.findById(clientId).ifPresent(client -> {
                client.incrementRequestCount(count);
                clientRepository.save(client);
                log.debug("Updated request count for client {}: +{}", clientId, count);
            });

            // 카운트 리셋
            redisTemplate.opsForValue().set(key, "0");

        } catch (Exception e) {
            log.error("Failed to sync API calls for key: {}", key, e);
        }
    }

    private Integer extractClientId(String key) {
        // client:123:count 형식에서 123 추출
        String[] parts = key.split(":");
        return Integer.parseInt(parts[1]);
    }
}
