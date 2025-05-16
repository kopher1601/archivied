package jp.co.topucomunity.backend_java.recruitments.repository;

import jp.co.topucomunity.backend_java.recruitments.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentsRepository extends JpaRepository<Recruitment, Long>, RecruitmentsRepositoryCustom {
}
