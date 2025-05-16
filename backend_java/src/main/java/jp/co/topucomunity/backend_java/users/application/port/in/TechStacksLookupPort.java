package jp.co.topucomunity.backend_java.users.application.port.in;

import jp.co.topucomunity.backend_java.recruitments.domain.TechStack;

import java.util.Optional;

public interface TechStacksLookupPort {
    Optional<TechStack> findByTechnologyName(String technologyName);

}
