
package com.bookstore.bookpreorder.service;

import com.bookstore.bookpreorder.dto.BookCreateRequest;
import com.bookstore.bookpreorder.dto.BookResponse;
import com.bookstore.bookpreorder.dto.BookUpdateRequest;
import com.bookstore.bookpreorder.exception.ResourceNotFoundException;
import com.bookstore.bookpreorder.model.Book;
import com.bookstore.bookpreorder.model.Category;
import com.bookstore.bookpreorder.repository.BookRepository;
import com.bookstore.bookpreorder.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    // Создание книги
    public BookResponse createBook(BookCreateRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категория не найдена"
                ));

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .price(request.getPrice())
                .releaseDate(request.getReleaseDate())
                .stockQuantity(request.getStockQuantity())
                .category(category)
                .build();

        Book savedBook = bookRepository.save(book);

        return toResponse(savedBook);
    }

    // Получение всех книг
    public List<BookResponse> getAllBooks() {

        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Получение книги по ID
    public BookResponse getBookById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книга с ID " + id + " не найдена"
                ));

        return toResponse(book);
    }

    // Поиск книг по названию
    public List<BookResponse> searchBooksByTitle(String title) {

        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Поиск книг по автору
    public List<BookResponse> searchBooksByAuthor(String author) {

        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Получение книг по категории
    public List<BookResponse> getBooksByCategory(Long categoryId) {

        return bookRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Обновление книги
    public BookResponse updateBook(Long id, BookUpdateRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книга с ID " + id + " не найдена"
                ));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категория не найдена"
                ));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setReleaseDate(request.getReleaseDate());
        book.setStockQuantity(request.getStockQuantity());
        book.setCategory(category);

        Book updatedBook = bookRepository.save(book);

        return toResponse(updatedBook);
    }

    // Удаление книги
    public void deleteBook(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книга с ID " + id + " не найдена"
                ));

        bookRepository.delete(book);
    }

    // Преобразование Entity в DTO
    private BookResponse toResponse(Book book) {

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getDescription(),
                book.getPrice(),
                book.getReleaseDate(),
                book.getStockQuantity(),
                book.getCategory() != null
                        ? book.getCategory().getName()
                        : null
        );
    }
}
