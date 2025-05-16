package jp.co.topucomunity.backend_java.recruitments.repository;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionsRepository extends JpaRepository<Position, Long> {

    /**
     * expect
     *
     * select *
     * from position p
     * where p.position_name = ?
     *
     */
    Optional<Position> findPositionByPositionName(String positionName);
}
