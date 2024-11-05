package com.inglo.giggle.notification.application.controller.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inglo.giggle.notification.application.dto.request.NotificationRequestDto;
import com.inglo.giggle.notification.application.usecase.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationConsumerController {

    private final ObjectMapper objectMapper;
    private final SendNotificationUseCase sendNotificationUseCase;

    @KafkaListener(
            topics = "api.notification",
            groupId = "api-server"
    )
    public void consumeNotificationEvent(
            ConsumerRecord<String, Map<String, Object>> record
    ) {
        Map<String, Object> payload = record.value();

        NotificationRequestDto notificationRequestDto = objectMapper.convertValue(payload, NotificationRequestDto.class);

        sendNotificationUseCase.execute(
                notificationRequestDto
        );

    }

}
