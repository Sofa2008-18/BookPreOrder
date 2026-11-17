
package com.bookstore.bookpreorder.service;

import com.bookstore.bookpreorder.dto.LoginRequest;
import com.bookstore.bookpreorder.dto.RegisterRequest;
import com.bookstore.bookpreorder.exception.ResourceNotFoundException;
import com.bookstore.bookpreorder.model.Role;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Регистрация пользователя
    public User registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Пользователь с таким email уже существует"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    // Авторизация пользователя
    public User loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Неверный email или пароль")
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Неверный email или пароль");
        }

        return user;
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получение пользователя по ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден")
                );
    }

    // Получение пользователя по email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Пользователь не найден")
                );
    }

    // Изменение имени пользователя
    @Transactional
    public User updateUserName(Long id, String newName) {

        User user = getUserById(id);
        user.setName(newName);

        return userRepository.save(user);
    }

    // Изменение роли пользователя
    @Transactional
    public User updateUserRole(Long id, Role newRole) {

        User user = getUserById(id);
        user.setRole(newRole);

        return userRepository.save(user);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {

        User user = getUserById(id);
        userRepository.delete(user);
    }
}
