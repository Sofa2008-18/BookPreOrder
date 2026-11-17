
package com.bookstore.bookpreorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {

    @NotEmpty(message = "Заказ должен содержать хотя бы одну книгу")
    @Valid
    private List<OrderItemRequest> items;
}
