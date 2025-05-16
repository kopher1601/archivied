package jp.co.topucomunity.backend_java.recruitments.controller;

import jp.co.topucomunity.backend_java.recruitments.controller.in.CreateRecruitmentRequest;
import jp.co.topucomunity.backend_java.recruitments.controller.in.RecruitmentSearch;
import jp.co.topucomunity.backend_java.recruitments.controller.in.UpdateRecruitmentRequest;
import jp.co.topucomunity.backend_java.recruitments.controller.out.RecruitmentResponse;
import jp.co.topucomunity.backend_java.recruitments.controller.out.RecruitmentSearchResult;
import jp.co.topucomunity.backend_java.recruitments.usecase.RecruitmentsUsecase;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.PostRecruitment;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.UpdateRecruitment;
import jp.co.topucomunity.backend_java.users.domain.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/recruitments")
@RestController
@RequiredArgsConstructor
public class RecruitmentsController {

    private final RecruitmentsUsecase recruitmentsUsecase;

    @PostMapping
    public void createRecruitment(@RequestBody @Validated CreateRecruitmentRequest request, @Validated UserSession session) {
        recruitmentsUsecase.post(PostRecruitment.of(request, Long.valueOf(session.id())));
    }

    @GetMapping("/{recruitmentId}")
    public RecruitmentResponse getRecruitmentById(@PathVariable Long recruitmentId) { // TODO : recruitmentIdのnullバリデーション
        return recruitmentsUsecase.getRecruitment(recruitmentId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{recruitmentId}")
    public void deleteRecruitmentById(@PathVariable Long recruitmentId) { // TODO : recruitmentIdのnullバリデーション
        recruitmentsUsecase.deleteRecruitment(recruitmentId);
    }

    @GetMapping("/query")
    public RecruitmentSearchResult getRecruitmentsForIndexPage(@ModelAttribute RecruitmentSearch recruitmentSearch) {
        return recruitmentsUsecase.getRecruitments(recruitmentSearch);
    }

    @PutMapping("/{recruitmentId}")
    public void updateByRecruitmentId(@PathVariable Long recruitmentId, @RequestBody @Validated UpdateRecruitmentRequest request) {
        recruitmentsUsecase.update(recruitmentId, UpdateRecruitment.from(request));
    }

}
