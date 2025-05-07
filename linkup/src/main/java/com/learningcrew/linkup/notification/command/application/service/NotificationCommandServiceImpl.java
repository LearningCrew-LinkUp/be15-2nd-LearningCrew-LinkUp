package com.learningcrew.linkup.notification.command.application.service;

import com.learningcrew.linkup.notification.command.application.dto.CreateNotificationResponse;
import com.learningcrew.linkup.notification.command.application.dto.EventNotificationRequest;
import com.learningcrew.linkup.notification.command.application.dto.MarkNotificationReadResponse;
import com.learningcrew.linkup.notification.command.application.dto.NotificationSettingRequest;
import com.learningcrew.linkup.notification.command.domain.aggregate.Notification;
import com.learningcrew.linkup.notification.command.domain.aggregate.NotificationEnumStatus;
import com.learningcrew.linkup.notification.command.domain.aggregate.NotificationSetting;
import com.learningcrew.linkup.notification.command.domain.aggregate.NotificationType;
import com.learningcrew.linkup.notification.command.domain.repository.NotificationRepository;
import com.learningcrew.linkup.notification.command.domain.repository.NotificationSettingRepository;
import com.learningcrew.linkup.notification.command.domain.repository.NotificationTypeRepository;
import com.learningcrew.linkup.notification.command.domain.service.NotificationDomainService;
import com.learningcrew.linkup.notification.command.infrastructure.GmailNotificationClient;
import com.learningcrew.linkup.notification.command.infrastructure.NotificationSseService;
import com.learningcrew.linkup.notification.command.util.NotificationTemplateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final NotificationDomainService notificationDomainService;
    private final GmailNotificationClient gmailNotificationClient;
    private final NotificationSseService notificationSseService;

    @Override
    public CreateNotificationResponse sendEventNotification(EventNotificationRequest request) {
        Integer receiverId = request.getReceiverId();
        Integer notificationTypeId = request.getNotificationTypeId();

        NotificationType notificationType = notificationTypeRepository.findById(notificationTypeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림 유형입니다."));

        Optional<NotificationSetting> settingOpt =
                notificationSettingRepository.findByUserIdAndNotificationTypeId(receiverId, notificationTypeId);
        boolean isAllowed = settingOpt.map(NotificationSetting::isEnabled).orElse(false);
        if (!isAllowed) {
            log.info("🔕 수신 차단된 유저 - userId: {}, typeId: {}", receiverId, notificationTypeId);
            return new CreateNotificationResponse(null);
        }

        Map<String, String> variables = request.getVariables();

        log.info("🧪 전달된 변수 확인: {}", request.getVariables());

        String processedTitle = NotificationTemplateProcessor.process(notificationType.getNotificationType(), variables);
        String processedContent = NotificationTemplateProcessor.process(notificationType.getNotificationTemplate(), variables);

        log.info("📨 바인딩된 제목: {}, 내용: {}", processedTitle, processedContent);


        Notification notification = new Notification(
                processedTitle,
                processedContent,
                receiverId,
                request.getDomainTypeId(),
                notificationTypeId
        );
        notification.setIsRead(NotificationEnumStatus.N);

        Notification savedNotification = notificationRepository.save(notification);
        notificationDomainService.processNotification(savedNotification);

        // 📧 이메일 전송
        if (notificationType.getSendEmail() == NotificationEnumStatus.Y) {
            try {
                gmailNotificationClient.sendEmailNotification(
                        String.valueOf(receiverId),
                        savedNotification.getTitle(),
                        savedNotification.getContent()
                );
            } catch (Exception e) {
                log.warn("📧 이메일 전송 실패 - userId: {}, error: {}", receiverId, e.getMessage());
            }
        }

        // 📡 SSE 실시간 알림 전송
        try {
            notificationSseService.pushNotification(savedNotification);
        } catch (Exception e) {
            log.warn("📡 SSE 전송 실패 - userId: {}, error: {}", receiverId, e.getMessage());
        }

        return new CreateNotificationResponse(savedNotification.getId());
    }

    @Override
    public void updateNotificationSetting(Integer userId, NotificationSettingRequest request) {
        Optional<NotificationSetting> existingSetting = notificationSettingRepository
                .findByUserIdAndNotificationTypeId(userId, request.getNotificationTypeId());

        if (existingSetting.isPresent()) {
            NotificationSetting setting = existingSetting.get();
            setting.setIsEnabled(request.isEnabled() ? "Y" : "N");
            notificationSettingRepository.save(setting);
        } else {
            NotificationSetting newSetting = new NotificationSetting();
            newSetting.setUserId(userId);
            newSetting.setNotificationTypeId(request.getNotificationTypeId());
            newSetting.setIsEnabled(request.isEnabled() ? "Y" : "N");
            notificationSettingRepository.save(newSetting);
        }
    }

    @Override
    public MarkNotificationReadResponse markNotificationAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id " + notificationId));
        notification.markAsRead();
        notificationRepository.save(notification);
        boolean readStatus = notification.getIsRead() == NotificationEnumStatus.Y;
        return new MarkNotificationReadResponse(notification.getId(), readStatus);
    }
}
