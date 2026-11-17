
package com.bookstore.bookpreorder.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreorderCreateRequest {

    @NotNull(message = "Необходимо указать ID книги")
    private Long bookId;
}
