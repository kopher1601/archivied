package jp.co.topucomunity.backend_java.recruitments.repository;

import jp.co.topucomunity.backend_java.recruitments.domain.RecruitmentTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentTechStacksRepository extends JpaRepository<RecruitmentTechStack, Long> {
}
