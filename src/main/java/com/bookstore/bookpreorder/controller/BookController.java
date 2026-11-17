
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.BookCreateRequest;
import com.bookstore.bookpreorder.dto.BookResponse;
import com.bookstore.bookpreorder.dto.BookUpdateRequest;
import com.bookstore.bookpreorder.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Создание книги
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(
            @Valid @RequestBody BookCreateRequest request) {

        return bookService.createBook(request);
    }

    // Получение всех книг
    @GetMapping
    public List<BookResponse> getAllBooks() {

        return bookService.getAllBooks();
    }

    // Получение книги по ID
    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id) {

        return bookService.getBookById(id);
    }

    // Поиск книг по названию
    @GetMapping("/search/title")
    public List<BookResponse> searchBooksByTitle(
            @RequestParam String title) {

        return bookService.searchBooksByTitle(title);
    }

    // Поиск книг по автору
    @GetMapping("/search/author")
    public List<BookResponse> searchBooksByAuthor(
            @RequestParam String author) {

        return bookService.searchBooksByAuthor(author);
    }

    // Получение книг по категории
    @GetMapping("/category/{categoryId}")
    public List<BookResponse> getBooksByCategory(
            @PathVariable Long categoryId) {

        return bookService.getBooksByCategory(categoryId);
    }

    // Обновление книги
    @PutMapping("/{id}")
    public BookResponse updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {

        return bookService.updateBook(id, request);
    }

    // Удаление книги
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);
    }
}
