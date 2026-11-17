
package com.bookstore.bookpreorder.service;

import com.bookstore.bookpreorder.dto.OrderCreateRequest;
import com.bookstore.bookpreorder.dto.OrderHistoryResponse;
import com.bookstore.bookpreorder.dto.OrderItemRequest;
import com.bookstore.bookpreorder.dto.OrderItemResponse;
import com.bookstore.bookpreorder.dto.OrderResponse;
import com.bookstore.bookpreorder.exception.ResourceNotFoundException;
import com.bookstore.bookpreorder.model.Book;
import com.bookstore.bookpreorder.model.Order;
import com.bookstore.bookpreorder.model.OrderItem;
import com.bookstore.bookpreorder.model.OrderStatus;
import com.bookstore.bookpreorder.model.OrderStatusHistory;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.repository.BookRepository;
import com.bookstore.bookpreorder.repository.OrderItemRepository;
import com.bookstore.bookpreorder.repository.OrderRepository;
import com.bookstore.bookpreorder.repository.OrderStatusHistoryRepository;
import com.bookstore.bookpreorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы одну книгу");
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        Order savedOrder = orderRepository.save(order);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Количество книг должно быть больше нуля");
            }

            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена"));

            if (book.getStockQuantity() == null ||
                    book.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Недостаточно книг на складе: " + book.getTitle());
            }

            book.setStockQuantity(book.getStockQuantity() - itemRequest.getQuantity());
            bookRepository.save(book);

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .book(book)
                    .quantity(itemRequest.getQuantity())
                    .price(book.getPrice())
                    .build();

            orderItemRepository.save(orderItem);

            BigDecimal subtotal = book.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalAmount = totalAmount.add(subtotal);
        }

        savedOrder.setTotalAmount(totalAmount);
        orderRepository.save(savedOrder);

        saveHistory(savedOrder, OrderStatus.PENDING, "Заказ создан");

        return toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден"));

        return toResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден"));

        if (newStatus == null) {
            throw new IllegalArgumentException("Статус не должен быть пустым");
        }

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException(
                    "Нельзя изменить статус завершённого или отменённого заказа");
        }

        if (order.getStatus() == newStatus) {
            throw new IllegalArgumentException("Заказ уже имеет такой статус");
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        saveHistory(updatedOrder, newStatus, "Статус заказа изменён");

        return toResponse(updatedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderHistoryResponse> getOrderHistory(Long orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ не найден"));

        return orderStatusHistoryRepository
                .findByOrderIdOrderByChangedAtDesc(orderId)
                .stream()
                .map(history -> new OrderHistoryResponse(
                        history.getId(),
                        history.getOrder().getId(),
                        history.getStatus().name(),
                        history.getChangedAt(),
                        history.getComment()
                ))
                .toList();
    }

    private void saveHistory(Order order, OrderStatus status, String comment) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .status(status)
                .changedAt(LocalDateTime.now())
                .comment(comment)
                .build();

        orderStatusHistoryRepository.save(history);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = orderItemRepository
                .findByOrderId(order.getId())
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity())
                        )
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getOrderDate(),
                order.getStatus().name(),
                order.getTotalAmount(),
                items
        );
    }
}
