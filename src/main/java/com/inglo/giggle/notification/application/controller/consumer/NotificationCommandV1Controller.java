package com.inglo.giggle.notification.application.controller.consumer;

import com.inglo.giggle.notification.application.dto.request.NotificationRequestDto;
import com.inglo.giggle.notification.application.usecase.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class NotificationCommandV1Controller {

    private final SendNotificationUseCase sendNotificationUseCase;

    @PostMapping("/notification")
    public void sendNotification(
            @RequestBody NotificationRequestDto notificationRequestDto
    ) {
        sendNotificationUseCase.execute(
                notificationRequestDto
        );
    }

}
