package com.inglo.giggle.notification.application.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.inglo.giggle.core.exception.error.ErrorCode;
import com.inglo.giggle.core.exception.type.CommonException;
import com.inglo.giggle.core.utility.RestClientUtil;
import com.inglo.giggle.notification.application.dto.request.NotificationRequestDto;
import com.inglo.giggle.notification.application.usecase.SendNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationService implements SendNotificationUseCase {

    private final RestClientUtil restClientUtil;

    @Value("${google.firebase.config.path}")
    private String firebaseConfigPath;

    @Value("${google.access-token.request.url}")
    private String GOOGLE_ACCESS_TOKEN_URL;

    @Value("${google.notification.url}")
    private String GOOGLE_NOTIFICATION_URL;

    private static final String BEARER_PREFIX = "Bearer " ;
    private static final String JSON_BODY_KEY_TITLE = "title";
    private static final String JSON_BODY_KEY_BODY = "body";
    private static final String JSON_BODY_KEY_TOKEN = "token";
    private static final String JSON_BODY_KEY_NOTIFICATION = "notification";
    private static final String JSON_BODY_KEY_MESSAGE = "message";
    private static final String JSON_BODY_KEY_VALIDATE_ONLY = "validate_only";


    @Override
    public void execute(NotificationRequestDto notificationRequestDto) {
        if(notificationRequestDto.deviceToken() == null){
            log.error("디바이스 토큰이 없습니다.");
            throw new CommonException(ErrorCode.INVALID_ARGUMENT);
        }

        JSONObject jsonBody = createJsonBody(
                notificationRequestDto.title(),
                notificationRequestDto.description(),
                notificationRequestDto.deviceToken()
        );
        restClientUtil.sendPostMethod(GOOGLE_NOTIFICATION_URL, getAccessToken(), jsonBody);
    }

    /* -------------------------------------------- */
    /* Private Methods ---------------------------- */
    /* -------------------------------------------- */

    private String getAccessToken(){
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of(GOOGLE_ACCESS_TOKEN_URL));

            credentials.refreshIfExpired();

            return BEARER_PREFIX + credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("Google Access Token을 가져오는데 실패했습니다.", e);
            throw new CommonException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }
    }

    private JSONObject createJsonBody(String title, String body, String token) {
        JSONObject messageBody = new JSONObject();

        JSONObject notificationJson = new JSONObject();
        notificationJson.put(JSON_BODY_KEY_TITLE, title);
        notificationJson.put(JSON_BODY_KEY_BODY, body);

        JSONObject messageJson = new JSONObject();
        messageJson.put(JSON_BODY_KEY_TOKEN, token);
        messageJson.put(JSON_BODY_KEY_NOTIFICATION, notificationJson);

        messageBody.put(JSON_BODY_KEY_MESSAGE, messageJson);
        messageBody.put(JSON_BODY_KEY_VALIDATE_ONLY, false);

        return messageBody;
    }

}
