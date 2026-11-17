package com.bookstore.bookpreorder.repository;

import com.bookstore.bookpreorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}