package jp.co.topucomunity.backend_java.recruitments.controller.out;

import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentSearchResult {

    private final Long count;
    private final List<RecruitmentIndexPageResponse> data;

    private RecruitmentSearchResult(Long count, List<RecruitmentIndexPageResponse> data) {
        this.count = count;
        this.data = data;
    }

    public static RecruitmentSearchResult from(Long count, List<RecruitmentIndexPageResponse> data) {
        return new RecruitmentSearchResult(count, data);
    }
}


