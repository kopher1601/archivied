package jp.co.topucomunity.backend_java.recruitments.controller;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.topucomunity.backend_java.recruitments.controller.out.RecruitmentErrorResponse;
import jp.co.topucomunity.backend_java.recruitments.exception.RecruitmentException;
import jp.co.topucomunity.backend_java.recruitments.exception.RecruitmentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final MessageSource messageSource;

    @ExceptionHandler(RecruitmentNotFoundException.class)
    public ResponseEntity<RecruitmentErrorResponse> recruitmentNotFoundHandler(RecruitmentException e, HttpServletRequest request) {

        String errorMessage = messageSource.getMessage(e.getMessage(), null, request.getLocale());

        return ResponseEntity.status(e.getStatusCode())
                .body(RecruitmentErrorResponse.from(errorMessage));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public RecruitmentErrorResponse invalidRequestHandler(MethodArgumentNotValidException e, HttpServletRequest request) {

        String errorMessage = messageSource.getMessage("recruitment.invalidRequest", null, request.getLocale());
        RecruitmentErrorResponse errorResponse = RecruitmentErrorResponse.from(errorMessage);

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            String validationErrorMessage = messageSource.getMessage(fieldError, request.getLocale());
            errorResponse.addValidation(fieldError.getField(), validationErrorMessage);
        }

        return errorResponse;
    }

//    @ExceptionHandler({Exception.class})
//    @ResponseBody
//    public void globalExceptionHandler(Exception e, HttpServletRequest request) {
//
//        log.error(e.getMessage(), e);
//    }
}
