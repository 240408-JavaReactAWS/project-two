package com.revature.nile.repositories;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1 AND status = ?2", nativeQuery = true)
    Optional<Order> findByUserIdAndStatus(int userId, String status);

    List<Item> findByOrderId(int itemId);
}
