package com.learningcrew.linkup.notification.command.application.helper;

import com.learningcrew.linkup.notification.command.application.dto.EventNotificationRequest;
import com.learningcrew.linkup.notification.command.application.service.NotificationCommandService;
import com.learningcrew.linkup.notification.command.domain.aggregate.NotificationType;
import com.learningcrew.linkup.notification.command.domain.repository.NotificationTypeRepository;
import com.learningcrew.linkup.notification.command.util.NotificationTemplateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationHelper {

    private final NotificationCommandService notificationCommandService;
    private final NotificationTypeRepository notificationTypeRepository;

    /**
     * 다른 도메인에서 알림을 전송할 때 사용 (기존 방식)
     * @param receiverId 알림 받을 사용자 ID
     * @param notificationTypeId 알림 유형 ID
     * @param domainTypeId 알림 도메인 구분 ID
     */
    public void sendBasicNotification(Integer receiverId, Integer notificationTypeId, Integer domainTypeId) {
        EventNotificationRequest request = new EventNotificationRequest();
        request.setReceiverId(receiverId);
        request.setNotificationTypeId(notificationTypeId);
        request.setDomainTypeId(domainTypeId);

        notificationCommandService.sendEventNotification(request);
    }

    /**
     * 다른 도메인에서 알림을 전송할 때, 변수 바인딩 포함하여 직접 요청 객체 전달
     * @param request EventNotificationRequest (receiverId, typeId, domainId, variables 포함)
     */
    public void sendNotification(EventNotificationRequest request) {
        notificationCommandService.sendEventNotification(request);

    }
}