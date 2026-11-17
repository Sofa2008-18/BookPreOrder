
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.RoleUpdateRequest;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // Изменение роли пользователя
    @PatchMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request
    ) {
        User user = userService.updateUserRole(
                id,
                request.getRole()
        );

        return "Роль пользователя " + user.getEmail()
                + " успешно изменена на " + user.getRole();
    }
}
