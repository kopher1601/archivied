package jp.co.topucomunity.backend_java.recruitments.usecase;

import jp.co.topucomunity.backend_java.recruitments.controller.in.RecruitmentSearch;
import jp.co.topucomunity.backend_java.recruitments.controller.out.RecruitmentIndexPageResponse;
import jp.co.topucomunity.backend_java.recruitments.controller.out.RecruitmentResponse;
import jp.co.topucomunity.backend_java.recruitments.controller.out.RecruitmentSearchResult;
import jp.co.topucomunity.backend_java.recruitments.domain.*;
import jp.co.topucomunity.backend_java.recruitments.exception.RecruitmentNotFoundException;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import jp.co.topucomunity.backend_java.recruitments.repository.RecruitmentsRepository;
import jp.co.topucomunity.backend_java.recruitments.repository.TechStacksRepository;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.PostRecruitment;
import jp.co.topucomunity.backend_java.recruitments.usecase.in.UpdateRecruitment;
import jp.co.topucomunity.backend_java.users.exception.TopuAuthNotFoundException;
import jp.co.topucomunity.backend_java.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentsUsecase {

    private final RecruitmentsRepository recruitmentsRepository;
    private final TechStacksRepository techStacksRepository;
    private final PositionsRepository positionsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void post(PostRecruitment postRecruitment) {

        var recruitment = Recruitment.from(postRecruitment);

        var foundUser = userRepository.findById(postRecruitment.getUserId())
                .orElseThrow(TopuAuthNotFoundException::new);

        // relationship between recruitment, techStack, and recruitmentTechStack.
        postRecruitment.getTechStacks().stream()
                .map(techName -> techStacksRepository.findByTechnologyName(techName).orElse(TechStack.from(techName)))
                .forEach(techStack -> {
                    var recruitmentTechStack = RecruitmentTechStack.of(techStack, recruitment);
                    recruitmentTechStack.makeRelationship(techStack, recruitment);
                });

        // relationship between recruitment, position, and recruitmentPosition.
        postRecruitment.getRecruitmentPositions().stream()
                .map(positionName -> positionsRepository.findPositionByPositionName(positionName).orElse(Position.from(positionName)))
                .forEach(position -> {
                    var recruitmentPosition = RecruitmentPosition.of(position, recruitment);
                    recruitmentPosition.makeRelationship(position, recruitment);
                });

        // relationship
        recruitment.makeRelationshipWithRecruitmentUser(foundUser);

        recruitmentsRepository.save(recruitment);
    }

    @Transactional
    public RecruitmentResponse getRecruitment(Long recruitmentId) {
        var foundRecruitment = recruitmentsRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);
        foundRecruitment.plusViews();

        return RecruitmentResponse.from(foundRecruitment);
    }

    public void deleteRecruitment(Long recruitmentId) {
        recruitmentsRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);

        recruitmentsRepository.deleteById(recruitmentId);
    }

    public RecruitmentSearchResult getRecruitments(RecruitmentSearch recruitmentSearch) {
        var recruitments = recruitmentsRepository.getSearchResult(recruitmentSearch);

        var data = recruitments.stream()
                .map(RecruitmentIndexPageResponse::from).toList();
        return RecruitmentSearchResult.from(Long.valueOf(data.size()), data);
    }

    @Transactional
    public void update(Long recruitmentId, UpdateRecruitment updateRecruitment) {

        var recruitment = recruitmentsRepository.findById(recruitmentId)
                .orElseThrow(RecruitmentNotFoundException::new);

        recruitment.clearTechStacksAndPositions();

        // relationship between recruitment, techStack, and recruitmentTechStack.
        updateRecruitment.getTechStacks().stream()
                .map(techName -> techStacksRepository.findByTechnologyName(techName).orElse(TechStack.from(techName)))
                .forEach(techStack -> {
                    var recruitmentTechStack = RecruitmentTechStack.of(techStack, recruitment);
                    recruitmentTechStack.makeRelationship(techStack, recruitment);
                });

        //relationship between recruitment, position, and recruitmentPosition.
        updateRecruitment.getRecruitmentPositions().stream()
                .map(positionName -> positionsRepository.findPositionByPositionName(positionName).orElse(Position.from(positionName)))
                .forEach(position -> {
                    var recruitmentPosition = RecruitmentPosition.of(position, recruitment);
                    recruitmentPosition.makeRelationship(position, recruitment);
                });

        recruitment.update(updateRecruitment);
    }

}