
package com.bookstore.bookpreorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    @NotNull(message = "Необходимо указать ID книги")
    private Long bookId;

    @NotNull(message = "Необходимо указать количество")
    @Min(value = 1, message = "Количество должно быть не меньше 1")
    private Integer quantity;
}

