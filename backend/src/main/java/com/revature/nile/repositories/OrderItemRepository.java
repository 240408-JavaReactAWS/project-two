package com.revature.nile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

    // OrderItem findByItemIdAndOrder(int itemId, Order order);
    void deleteByItemItemId(int itemId);
    Optional<OrderItem> findByItemItemIdAndOrderUserUserId(int itemId, int userId);
    Optional<List<OrderItem>> findAllByItemItemId(int itemId);
    
}
