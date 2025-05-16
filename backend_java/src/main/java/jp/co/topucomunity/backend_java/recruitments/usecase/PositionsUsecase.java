package jp.co.topucomunity.backend_java.recruitments.usecase;

import jp.co.topucomunity.backend_java.recruitments.controller.out.PositionResponse;
import jp.co.topucomunity.backend_java.recruitments.domain.Position;
import jp.co.topucomunity.backend_java.recruitments.repository.PositionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionsUsecase {

    private final PositionsRepository positionsRepository;

    public PositionResponse getPositions() {
        var positions = positionsRepository.findAll();
        return PositionResponse.from(positions.stream().map(Position::getPositionName).toList());
    }
}
