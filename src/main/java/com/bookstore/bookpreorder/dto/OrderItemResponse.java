
package com.bookstore.bookpreorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemResponse {

    private Long bookId;
    private String bookTitle;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
