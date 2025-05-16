package jp.co.topucomunity.backend_java.recruitments.repository;

import jp.co.topucomunity.backend_java.recruitments.domain.RecruitmentPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentPositionsRepository extends JpaRepository<RecruitmentPosition, Long> {
}
