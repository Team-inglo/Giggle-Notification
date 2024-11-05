package com.inglo.giggle.core.utility;

import com.inglo.giggle.core.exception.error.ErrorCode;
import com.inglo.giggle.core.exception.type.CommonException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class RestClientUtil {

    private final RestClient restClient = RestClient.create();

    public void sendPostMethod(String url, String token, JSONObject jsonObject) {
        restClient.post()
                .uri(url)
                .header("Authorization", token)
                .header("Content-Type", "application/json; UTF-8")
                .body(jsonObject)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new CommonException(ErrorCode.INVALID_ARGUMENT);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR);
                })
                .toBodilessEntity();
    }

}
