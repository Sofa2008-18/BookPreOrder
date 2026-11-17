
package com.bookstore.bookpreorder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "preorders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preorder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDateTime preorderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreorderStatus status;
}
