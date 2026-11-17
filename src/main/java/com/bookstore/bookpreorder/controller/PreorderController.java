
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.PreorderCreateRequest;
import com.bookstore.bookpreorder.dto.PreorderResponse;
import com.bookstore.bookpreorder.dto.PreorderStatusUpdateRequest;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.service.PreorderService;
import com.bookstore.bookpreorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preorders")
@RequiredArgsConstructor
public class PreorderController {

    private final PreorderService preorderService;
    private final UserService userService;

    // Создание предзаказа текущим пользователем
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public PreorderResponse createPreorder(
            Authentication authentication,
            @Valid @RequestBody PreorderCreateRequest request
    ) {
        User user = userService.getUserByEmail(authentication.getName());

        return preorderService.createPreorder(user.getId(), request);
    }

    // Получение всех предзаказов — менеджер или администратор
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<PreorderResponse> getAllPreorders() {
        return preorderService.getAllPreorders();
    }

    // Получение предзаказов текущего пользователя
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<PreorderResponse> getMyPreorders(
            Authentication authentication
    ) {
        User user = userService.getUserByEmail(authentication.getName());

        return preorderService.getUserPreorders(user.getId());
    }

    // Получение предзаказа по ID — менеджер или администратор
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public PreorderResponse getPreorderById(
            @PathVariable Long id
    ) {
        return preorderService.getPreorderById(id);
    }

    // Изменение статуса предзаказа
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public PreorderResponse updatePreorderStatus(
            @PathVariable Long id,
            @Valid @RequestBody PreorderStatusUpdateRequest request
    ) {
        return preorderService.updatePreorderStatus(
                id,
                request.getStatus()
        );
    }
}
