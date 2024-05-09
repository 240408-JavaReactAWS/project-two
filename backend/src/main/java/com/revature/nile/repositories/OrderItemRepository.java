package com.revature.nile.repositories;

import com.revature.nile.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.nile.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

    OrderItem findByItemIdAndOrder(int itemId, Order order);
}
