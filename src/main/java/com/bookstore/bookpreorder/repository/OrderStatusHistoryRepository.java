
package com.bookstore.bookpreorder.repository;

import com.bookstore.bookpreorder.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStatusHistoryRepository
        extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findByOrderIdOrderByChangedAtDesc(Long orderId);
}
