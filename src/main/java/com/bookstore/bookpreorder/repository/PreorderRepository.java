
package com.bookstore.bookpreorder.repository;

import com.bookstore.bookpreorder.model.Preorder;
import com.bookstore.bookpreorder.model.PreorderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreorderRepository extends JpaRepository<Preorder, Long> {

    List<Preorder> findByUserId(Long userId);

    List<Preorder> findByStatus(PreorderStatus status);

}
