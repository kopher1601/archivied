package jp.co.topucomunity.backend_java.users.usecase;

import jp.co.topucomunity.backend_java.users.controller.out.UserResponse;
import jp.co.topucomunity.backend_java.users.exception.TopuAuthNotFoundException;
import jp.co.topucomunity.backend_java.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUsecase {

    private final UserRepository userRepository;

    public UserResponse getUser(Long userId) {
        var foundUser = userRepository.findById(userId).orElseThrow(TopuAuthNotFoundException::new);
        return UserResponse.from(foundUser);
    }
}
