package com.learningcrew.linkup.notification.command.infrastructure;

import com.learningcrew.linkup.notification.command.domain.aggregate.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class NotificationSseServiceImpl implements NotificationSseService {

//    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    @Override
//    public SseEmitter connect(Integer userId) {
//        SseEmitter emitter = new SseEmitter(60 * 10000L); // 10분 동안 연결 유지
//        emitters.put(userId, emitter);
//
//        emitter.onCompletion(() -> emitters.remove(userId));
//        emitter.onTimeout(() -> emitters.remove(userId));
//
//        try {
//            emitter.send(SseEmitter.event().name("connect").data("connected"));
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//        }
//
//        return emitter;
//    }
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter connect(Integer userId) {
        SseEmitter emitter = new SseEmitter(60 * 10000L);
        emitters.put(String.valueOf(userId), emitter);

        emitter.onCompletion(() -> emitters.remove(String.valueOf(userId)));
        emitter.onTimeout(() -> emitters.remove(String.valueOf(userId)));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }


    @Override
    public void registerEmitter(SseEmitter emitter) {
        // 이 메서드는 더 이상 사용되지 않지만, 구현 유지 (호환용)
    }

//    @Override
//    public void pushNotification(Notification notification) {
//        Integer userId = notification.getReceiverId();
//        log.info("📨 pushNotification 호출됨 - userId={}, title={}", userId, notification.getTitle());
//
//
//        SseEmitter emitter = emitters.get(userId);
//
//        if (emitter != null) {
//            try {
//                log.info("📡 SSE 전송 시작 - userId={}", userId);
//
//                Map<String, String> payload = Map.of(
//                        "content", notification.getContent(),
//                        "title", notification.getTitle()
//
//                        );
//
//                emitter.send(SseEmitter.event()
//                        .name("notification")
//                        .data(payload));
//
//                log.info("✅ SSE 전송 완료 - userId={}", userId);
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//                emitters.remove(userId);
//                log.error("❌ SSE 전송 실패 - userId {}: {}", userId, e.getMessage());
//            }
//        } else {
//            log.warn("⚠️ SSE Emitter 없음 - userId {}", userId);
//        }
//    }

    @Override
    public void pushNotification(Notification notification) {
        String userIdKey = String.valueOf(notification.getReceiverId());
        log.info("📨 pushNotification 호출됨 - userId={}, title={}", userIdKey, notification.getTitle());

        SseEmitter emitter = emitters.get(userIdKey);

        if (emitter != null) {
            try {
                log.info("📡 SSE 전송 시작 - userId={}", userIdKey);
                Map<String, String> payload = Map.of(
                        "content", notification.getContent(),
                        "title", notification.getTitle()
                );

                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(payload));

                log.info("✅ SSE 전송 완료 - userId={}", userIdKey);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(userIdKey);
                log.error("❌ SSE 전송 실패 - userId {}: {}", userIdKey, e.getMessage());
            }
        } else {
            log.warn("⚠️ SSE Emitter 없음 - userId {}", userIdKey);
        }
    }
}