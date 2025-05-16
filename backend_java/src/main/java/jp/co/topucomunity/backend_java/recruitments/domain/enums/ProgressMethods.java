package jp.co.topucomunity.backend_java.recruitments.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProgressMethods {
    ALL("全体"),
    ONLINE("オンライン"),
    OFFLINE("オフライン");

    private final String name;
}
