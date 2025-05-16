package jp.co.topucomunity.backend_java.users.repository;

import jp.co.topucomunity.backend_java.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * expect
     *
     * select *
     * from users u
     * where u.email = ?
     *
     */
    Optional<User> findUserByEmail(String emailAddress);

    /**
     * expect
     *
     * select *
     * from users u
     * where u.nickname = ?
     */
    Optional<User> findByNickname(String nickname);
}
