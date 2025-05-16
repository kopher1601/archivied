package jp.co.topucomunity.backend_java.users.adapter.in.infrastructure;

import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import jp.co.topucomunity.backend_java.users.application.port.in.PositionLookupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PositionLookupAdapter implements PositionLookupPort {

    private final PositionsRepository positionsRepository;

    @Override
    public Optional<Position> findPositionByPositionName(String positionName) {
        return positionsRepository.findPositionByPositionName(positionName);
    }
}
