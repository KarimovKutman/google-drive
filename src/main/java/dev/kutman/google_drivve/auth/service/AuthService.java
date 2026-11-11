package dev.kutman.google_drivve.auth.service;

import dev.kutman.google_drivve.auth.dto.AuthResponse;
import dev.kutman.google_drivve.auth.dto.LoginRequest;
import dev.kutman.google_drivve.auth.dto.RegisterRequest;
import dev.kutman.google_drivve.dto.UserResponse;
import dev.kutman.google_drivve.entity.User;
import dev.kutman.google_drivve.exception.EmailAlreadyExistsException;
import dev.kutman.google_drivve.exception.InvalidCredentialsException;
import dev.kutman.google_drivve.exception.UsernameAlreadyExistsException;
import dev.kutman.google_drivve.repository.UserRepository;
import dev.kutman.google_drivve.security.JwtService;
import dev.kutman.google_drivve.security.RefreshTokenService;
import dev.kutman.google_drivve.security.UserDetailsImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalize(request.email());
        String username = normalize(request.username());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException();
        }
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User(
                username,
                email,
                passwordEncoder.encode(request.password()),
                request.name().strip(),
                request.surname().strip()
        );

        User savedUser = userRepository.save(user);
        return createAuthResponse(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = normalize(request.email());
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {
        User user = refreshTokenService.consumeAndRotate(rawRefreshToken);
        return createAuthResponse(user);
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    private AuthResponse createAuthResponse(User user) {
        UserDetailsImpl principal = new UserDetailsImpl(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                "Bearer",
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpirationSeconds(),
                UserResponse.from(user)
        );
    }

    private String normalize(String value) {
        return value.strip().toLowerCase(Locale.ROOT);
    }
}
