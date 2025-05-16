package jp.co.topucomunity.backend_java.recruitments.controller.in;

import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * expected
 * <p>
 * ?page=1&size=20&techStacks=kotlin,java,spring&positions=backend,frontend&progressMethods=ALL&search=모집합니다
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentSearch {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 20;

    private RecruitmentCategories categories;
    private List<String> techStacks;
    private List<String> positions;
    private ProgressMethods progressMethods;
    private String search;

}
