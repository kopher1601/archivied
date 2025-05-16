package jp.co.topucomunity.backend_java.users.adapter.out.persistence;

import jp.co.topucomunity.backend_java.users.application.port.out.SignupPort;
import jp.co.topucomunity.backend_java.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserPersistenceAdapter implements SignupPort {

    private final AuthRepository authRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return authRepository.findById(userId);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return authRepository.findByNickname(nickname);
    }

}
