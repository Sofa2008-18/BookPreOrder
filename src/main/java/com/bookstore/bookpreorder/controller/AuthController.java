
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.AuthResponse;
import com.bookstore.bookpreorder.dto.LoginRequest;
import com.bookstore.bookpreorder.dto.RegisterRequest;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.security.JwtService;
import com.bookstore.bookpreorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse registerUser(
            @Valid @RequestBody RegisterRequest request
    ) {
        User user = userService.registerUser(request);

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token,
                "Регистрация прошла успешно"
        );
    }

    @PostMapping("/login")
    public AuthResponse loginUser(
            @Valid @RequestBody LoginRequest request
    ) {
        User user = userService.loginUser(request);

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                token,
                "Вход выполнен успешно"
        );
    }
}
