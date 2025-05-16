package jp.co.topucomunity.backend_java.recruitments.controller.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateRecruitmentRequest {

    @NotNull(message = "{recruitment.required}")
    private RecruitmentCategories recruitmentCategories;

    @NotNull(message = "{recruitment.required}")
    private ProgressMethods progressMethods;

    @NotNull(message = "{recruitment.required}")
    private List<String> techStacks;

    @NotNull(message = "{recruitment.required}")
    private List<String> recruitmentPositions;

    @NotNull(message = "{recruitment.required}")
    @Range(min = 1, message = "{recruitment.range.min}")
    private Integer numberOfPeople;

    @NotNull(message = "{recruitment.required}")
    @Range(min = 1, message = "{recruitment.range.min}")
    private Integer progressPeriod;

    @NotNull(message = "{recruitment.required}")
    @FutureOrPresent(message = "{recruitment.futureOrPresent.deadline}")
    private LocalDate recruitmentDeadline;

    @Email(message = "{recruitment.invalidEmail.contract}")
    @NotBlank(message = "{recruitment.required}")
    private String contract;

    @NotBlank(message = "{recruitment.required}")
    private String subject;

    @NotBlank(message = "{recruitment.required}")
    private String content;

}
