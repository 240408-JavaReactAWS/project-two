package com.revature.nile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.nile.models.OrderItem;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

    void deleteByItemItemId(int itemId);
    Optional<OrderItem> findByItemUserUserId(int userId);

    void deleteByItemItemIdAndOrderStatus(int itemId, String status);
    Optional<OrderItem> findByItemItemIdAndOrderUserUserId(int itemId, int userId);
}
