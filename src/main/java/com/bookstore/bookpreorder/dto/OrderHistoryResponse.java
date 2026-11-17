package com.bookstore.bookpreorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderHistoryResponse {

    private Long id;
    private Long orderId;
    private String status;
    private LocalDateTime changedAt;
    private String comment;
}