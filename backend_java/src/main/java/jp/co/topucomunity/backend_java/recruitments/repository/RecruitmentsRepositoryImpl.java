package jp.co.topucomunity.backend_java.recruitments.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jp.co.topucomunity.backend_java.recruitments.controller.in.RecruitmentSearch;
import jp.co.topucomunity.backend_java.recruitments.domain.Recruitment;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.ProgressMethods;
import jp.co.topucomunity.backend_java.recruitments.domain.enums.RecruitmentCategories;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.or;
import static jp.co.topucomunity.backend_java.recruitments.domain.QPosition.position;
import static jp.co.topucomunity.backend_java.recruitments.domain.QRecruitment.recruitment;
import static jp.co.topucomunity.backend_java.recruitments.domain.QRecruitmentPosition.recruitmentPosition;
import static jp.co.topucomunity.backend_java.recruitments.domain.QRecruitmentTechStack.recruitmentTechStack;
import static jp.co.topucomunity.backend_java.recruitments.domain.QTechStack.techStack;

@RequiredArgsConstructor
public class RecruitmentsRepositoryImpl implements RecruitmentsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Recruitment> getSearchResult(RecruitmentSearch searchParams) {
        var categories = searchParams.getCategories();
        var positionNames = searchParams.getPositions();
        var techStackNames = searchParams.getTechStacks();
        var progressMethods = searchParams.getProgressMethods();
        var subject = searchParams.getSearch();

        return queryFactory.selectFrom(recruitment)
                .join(recruitmentTechStack).on(recruitmentTechStack.recruitment.id.eq(recruitment.id))
                .join(techStack).on(recruitmentTechStack.techStack.id.eq(techStack.id))
                .join(recruitmentPosition).on(recruitmentPosition.recruitment.id.eq(recruitment.id))
                .join(position).on(recruitmentPosition.position.id.eq(position.id))
                .fetchJoin()
                .where(or(eqPositions(positionNames), eqTechStacks(techStackNames)),
                        eqCategories(categories),
                        eqProgressMethods(progressMethods),
                        eqSubject(subject))
                .offset((long) (searchParams.getPage() - 1) * searchParams.getSize())
                .limit(searchParams.getSize())
                .orderBy(recruitment.createdAt.desc())
                .fetch();
    }

    private BooleanBuilder eqPositions(List<String> positionNames) {
        if (positionNames == null || positionNames.isEmpty()) {
            return null;
        }
        BooleanBuilder builder = new BooleanBuilder();
        positionNames.forEach(positionName ->
                builder.or(recruitmentPosition.position.positionName.eq(positionName)));

        return builder;
    }

    private BooleanBuilder eqTechStacks(List<String> techStackNames) {
        if (techStackNames == null || techStackNames.isEmpty()) {
            return null;
        }
        BooleanBuilder builder = new BooleanBuilder();
        techStackNames.forEach(techStackName ->
                builder.or(recruitmentTechStack.techStack.technologyName.eq(techStackName)));

        return builder;
    }

    private BooleanExpression eqCategories(RecruitmentCategories recruitmentCategories) {
        if (recruitmentCategories == null) {
            return null;
        }
        return recruitment.recruitmentCategories.eq(recruitmentCategories);
    }

    private BooleanExpression eqProgressMethods(ProgressMethods progressMethods) {
        if (progressMethods == null) {
            return null;
        }
        return recruitment.progressMethods.eq(progressMethods);
    }

    private BooleanExpression eqSubject(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return recruitment.subject.containsIgnoreCase(title);
    }

}
