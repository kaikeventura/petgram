package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.LoginRequest;
import com.kaikeventura.petgram.dto.RegisterRequest;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        var user = new User(
                null,
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Set.of("USER"),
                null,
                null
        );

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }
}
