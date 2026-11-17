
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.OrderCreateRequest;
import com.bookstore.bookpreorder.dto.OrderHistoryResponse;
import com.bookstore.bookpreorder.dto.OrderResponse;
import com.bookstore.bookpreorder.dto.OrderStatusUpdateRequest;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.service.OrderService;
import com.bookstore.bookpreorder.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    // Создание заказа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public OrderResponse createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderCreateRequest request) {

        User user = userService.getUserByEmail(authentication.getName());

        return orderService.createOrder(user.getId(), request);
    }

    // Получение заказов текущего пользователя
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    public List<OrderResponse> getMyOrders(Authentication authentication) {

        User user = userService.getUserByEmail(authentication.getName());

        return orderService.getUserOrders(user.getId());
    }

    // Получение всех заказов
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Получение одного заказа
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Изменение статуса заказа
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public OrderResponse updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {

        return orderService.updateOrderStatus(id, request.getStatus());
    }

    // Получение истории изменения статусов заказа
    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<OrderHistoryResponse> getOrderHistory(@PathVariable Long id) {
        return orderService.getOrderHistory(id);
    }
}
