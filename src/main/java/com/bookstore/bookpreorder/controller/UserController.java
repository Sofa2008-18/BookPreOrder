
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.UserResponse;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return toResponse(userService.getUserById(id));
    }

    @GetMapping("/email")
    public UserResponse getUserByEmail(@RequestParam String email) {
        return toResponse(userService.getUserByEmail(email));
    }

    @PatchMapping("/{id}/name")
    public UserResponse updateUserName(
            @PathVariable Long id,
            @RequestParam String newName) {

        return toResponse(userService.updateUserName(id, newName));
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Пользователь успешно удалён";
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
