package com.inglo.giggle.notification.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record NotificationRequestDto(

        String title,
        String description,

        @JsonProperty("device_token")
        String deviceToken
) {

}
