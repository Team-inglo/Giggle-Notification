package com.inglo.giggle.notification.application.usecase;

import com.inglo.giggle.core.bean.UseCase;
import com.inglo.giggle.notification.application.dto.request.NotificationRequestDto;

@UseCase
public interface SendNotificationUseCase {

    /**
     * 알림 전송하기
     */
    void execute(
            NotificationRequestDto notificationRequestDto
    );
}
