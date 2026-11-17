
package com.bookstore.bookpreorder.dto;

import com.bookstore.bookpreorder.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdateRequest {

    @NotNull(message = "Необходимо указать новый статус заказа")
    private OrderStatus status;

    private String comment;
}
