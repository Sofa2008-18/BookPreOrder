
package com.bookstore.bookpreorder.service;

import com.bookstore.bookpreorder.dto.PreorderCreateRequest;
import com.bookstore.bookpreorder.dto.PreorderResponse;
import com.bookstore.bookpreorder.exception.ResourceNotFoundException;
import com.bookstore.bookpreorder.model.Book;
import com.bookstore.bookpreorder.model.Preorder;
import com.bookstore.bookpreorder.model.PreorderStatus;
import com.bookstore.bookpreorder.model.User;
import com.bookstore.bookpreorder.repository.BookRepository;
import com.bookstore.bookpreorder.repository.PreorderRepository;
import com.bookstore.bookpreorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PreorderService {

    private final PreorderRepository preorderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // Создание предзаказа
    @Transactional
    public PreorderResponse createPreorder(
            Long userId,
            PreorderCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь с ID " + userId + " не найден"
                ));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книга с ID " + request.getBookId() + " не найдена"
                ));

        if (book.getReleaseDate() == null
                || !book.getReleaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Эту книгу нельзя предзаказать"
            );
        }

        Preorder preorder = Preorder.builder()
                .user(user)
                .book(book)
                .preorderDate(LocalDateTime.now())
                .status(PreorderStatus.WAITING_FOR_RELEASE)
                .build();

        Preorder savedPreorder = preorderRepository.save(preorder);

        return toResponse(savedPreorder);
    }

    // Получение всех предзаказов
    public List<PreorderResponse> getAllPreorders() {

        return preorderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Получение предзаказов пользователя
    public List<PreorderResponse> getUserPreorders(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь с ID " + userId + " не найден"
                ));

        return preorderRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Получение предзаказа по ID
    public PreorderResponse getPreorderById(Long id) {

        Preorder preorder = preorderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Предзаказ с ID " + id + " не найден"
                ));

        return toResponse(preorder);
    }

    // Изменение статуса предзаказа
    @Transactional
    public PreorderResponse updatePreorderStatus(
            Long id,
            PreorderStatus newStatus) {

        Preorder preorder = preorderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Предзаказ с ID " + id + " не найден"
                ));

        preorder.setStatus(newStatus);

        Preorder updatedPreorder = preorderRepository.save(preorder);

        return toResponse(updatedPreorder);
    }

    // Преобразование Entity в DTO
    private PreorderResponse toResponse(Preorder preorder) {

        return new PreorderResponse(
                preorder.getId(),
                preorder.getUser().getId(),
                preorder.getBook().getId(),
                preorder.getBook().getTitle(),
                preorder.getPreorderDate(),
                preorder.getBook().getReleaseDate(),
                preorder.getStatus().name()
        );
    }
}
