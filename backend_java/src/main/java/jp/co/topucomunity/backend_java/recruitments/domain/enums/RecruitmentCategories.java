package jp.co.topucomunity.backend_java.recruitments.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecruitmentCategories {
    PROJECT("プロジェクト"),
    STUDY("勉強会");

    private final String name;
}
