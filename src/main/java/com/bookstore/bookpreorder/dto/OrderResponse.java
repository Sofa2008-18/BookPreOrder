
package com.bookstore.bookpreorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;
}

