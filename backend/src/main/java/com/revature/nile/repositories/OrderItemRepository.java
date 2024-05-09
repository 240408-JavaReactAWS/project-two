package com.revature.nile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.nile.models.OrderItem;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

    void deleteByItemItemId(int itemId);

    Optional<OrderItem> findByItemItemIdAndOrderUserUserId(int itemId, int userId);
}
