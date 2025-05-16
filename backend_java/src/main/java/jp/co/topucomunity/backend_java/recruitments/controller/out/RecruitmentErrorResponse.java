package jp.co.topucomunity.backend_java.recruitments.controller.out;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *   "errorMessage": "잘못된 요청입니다",
 *   "validationErrors: {
 *      "title": "1글자 입력해주세요",
 *      "contact": "메일 주소를 제대로 입력해 주세요"
 *   },
 * }
 */

@Getter
public class RecruitmentErrorResponse {

    private final String errorMessage;
    private final Map<String, String> validationErrors;

    @Builder
    private RecruitmentErrorResponse(String errorMessage, Map<String, String> validationErrors) {
        this.errorMessage = errorMessage;
        this.validationErrors = validationErrors != null ? validationErrors : new HashMap<>();
    }

    public static RecruitmentErrorResponse from(String errorMessage) {
        return RecruitmentErrorResponse.builder()
                .errorMessage(errorMessage)
                .validationErrors(new HashMap<>())
                .build();
    }

    public void addValidation(String fieldName, String message) {
        this.validationErrors.put(fieldName, message);
    }
}
