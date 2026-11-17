
package com.bookstore.bookpreorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private BigDecimal price;
    private LocalDate releaseDate;
    private Integer stockQuantity;
    private String categoryName;
}
