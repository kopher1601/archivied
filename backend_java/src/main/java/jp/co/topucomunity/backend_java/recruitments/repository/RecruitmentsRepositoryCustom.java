package jp.co.topucomunity.backend_java.recruitments.repository;

import jp.co.topucomunity.backend_java.recruitments.controller.in.RecruitmentSearch;
import jp.co.topucomunity.backend_java.recruitments.domain.Recruitment;

import java.util.List;


public interface RecruitmentsRepositoryCustom {

    List<Recruitment> getSearchResult(RecruitmentSearch search);
}
