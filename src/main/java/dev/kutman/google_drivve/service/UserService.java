package dev.kutman.google_drivve.service;

import dev.kutman.google_drivve.dto.UserResponse;
import dev.kutman.google_drivve.entity.User;
import dev.kutman.google_drivve.exception.UserNotFoundException;
import dev.kutman.google_drivve.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getProfileByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);
        return UserResponse.from(user);
    }
}
