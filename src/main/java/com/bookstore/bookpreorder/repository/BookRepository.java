package com.bookstore.bookpreorder.repository;

import com.bookstore.bookpreorder.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByCategoryId(Long categoryId);
}