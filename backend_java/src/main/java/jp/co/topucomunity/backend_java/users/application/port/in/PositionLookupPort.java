package jp.co.topucomunity.backend_java.users.application.port.in;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;

import java.util.Optional;

public interface PositionLookupPort {

    Optional<Position> findPositionByPositionName(String positionName);

}
