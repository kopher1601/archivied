package jp.co.topucomunity.backend_java.recruitments.exception;

import org.springframework.http.HttpStatus;

public class RecruitmentNotFoundException extends RecruitmentException {

    public static final String RECRUITMENT_NOT_FOUND = "recruitment.notFound";

    public RecruitmentNotFoundException() {
        super(RECRUITMENT_NOT_FOUND);
    }

    public RecruitmentNotFoundException(Throwable cause) {
        super(RECRUITMENT_NOT_FOUND, cause);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
