package jp.co.topucomunity.backend_java.recruitments.repository;

import jp.co.topucomunity.backend_java.recruitments.domain.RecruitmentUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentUserRepository extends JpaRepository<RecruitmentUser, Long> {
}
