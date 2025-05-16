package jp.co.topucomunity.backend_java.users.application.port.out;

import jp.co.topucomunity.backend_java.users.domain.User;

import java.util.Optional;

public interface SignupPort {

    Optional<User> findById(Long userId);

    Optional<User> findByNickname(String nickname);

}
