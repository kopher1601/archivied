package jp.co.topucomunity.backend_java.users.adapter.in.infrastructure;

import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;
import jp.co.topucomunity.backend_java.recruitments.repository.TechStacksRepository;
import jp.co.topucomunity.backend_java.users.application.port.in.TechStacksLookupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TechStacksLookupAdapter implements TechStacksLookupPort {

    private final TechStacksRepository techStacksRepository;

    @Override
    public Optional<TechStack> findByTechnologyName(String technologyName) {
        return techStacksRepository.findByTechnologyName(technologyName);
    }
}
