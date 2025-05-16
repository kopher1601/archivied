package jp.co.topucomunity.backend_java.recruitments.usecase;

import jp.co.topucomunity.backend_java.recruitments.controller.out.TechStackResponse;
import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.recruitments.repository.TechStacksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechStacksUsecase {

    private final TechStacksRepository techStacksRepository;

    public TechStackResponse getTechnologyNames() {
        var techStacks = techStacksRepository.findAll();

        return TechStackResponse
                .from(techStacks
                        .stream()
                        .map(TechStack::getTechnologyName)
                        .toList());
    }
}
