
package com.bookstore.bookpreorder.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookUpdateRequest {

    @NotBlank(message = "Название книги обязательно")
    private String title;

    @NotBlank(message = "Автор обязателен")
    private String author;

    private String isbn;

    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше нуля")
    private BigDecimal price;

    private LocalDate releaseDate;

    @NotNull(message = "Количество книг обязательно")
    @Min(value = 0, message = "Количество не может быть отрицательным")
    private Integer stockQuantity;

    @NotNull(message = "Необходимо выбрать категорию")
    private Long categoryId;
}
