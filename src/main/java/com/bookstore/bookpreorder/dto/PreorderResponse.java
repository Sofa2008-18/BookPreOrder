
package com.bookstore.bookpreorder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PreorderResponse {

    private Long id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime preorderDate;
    private LocalDate releaseDate;
    private String status;
}
