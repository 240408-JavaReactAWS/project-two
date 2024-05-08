package com.revature.nile.services;

import com.revature.nile.models.Order;
import com.revature.nile.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderService {
      private final OrderRepository orderRepository;

      @Autowired
      public OrderService(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
      }

      public Order createOrder(Order order) {
            return orderRepository.save(order);
      }

      public Order getOrderById(int orderId) {
            return orderRepository.findById(orderId).orElse(null);
      }
}
