package jp.co.topucomunity.backend_java.users.controller.out;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *   "error_message": "잘못된 요청입니다",
 *   "validation_errors: {
 *      "title": "1글자 입력해주세요",
 *      "contact": "메일 주소를 제대로 입력해 주세요"
 *   },
 * }
 */

@Getter
public class UserErrorResponse {

    private final String errorMessage;
    private final Map<String, String> validationErrors;

    @Builder
    private UserErrorResponse(String errorMessage, Map<String, String> validationErrors) {
        this.errorMessage = errorMessage;
        this.validationErrors = validationErrors;
    }

    public static UserErrorResponse from(String errorMessage) {
        return UserErrorResponse.builder()
                .errorMessage(errorMessage)
                .validationErrors(new HashMap<>())
                .build();
    }
}

