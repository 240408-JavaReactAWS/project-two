package com.revature.nile.services;

import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderService {
      private final OrderRepository orderRepository;
      private final OrderItemRepository orderItemRepository;

      @Autowired
      public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
            this.orderRepository = orderRepository;
            this.orderItemRepository = orderItemRepository;
      }

      public Order createOrder(Order order) {
            return orderRepository.save(order);
      }

      public Order getOrderById(int orderId) {
            return orderRepository.findById(orderId).orElse(null);
      }

      public Order getCurrentOrderByUserId(int userId) {
            Optional<Order> currOrder = orderRepository.findByUserIdAndStatus(userId, "PENDING");
            if (currOrder.isPresent()) {
                  return currOrder.get();
            }
            else {
                  return null;
            }
            
      }

      public OrderItem createOrderItem(OrderItem orderItem) {
            return orderItemRepository.save(orderItem);
      }

      public User getUserByOrderId(int orderId) {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                  return order.get().getUser();
            }
            return null;
      }

      public OrderItem getCurrentOrderItemByUserId(int userId ,OrderItem orderItem) {

            Optional<OrderItem> currOrderItem = orderItemRepository.findByItemUserUserId(userId);
            System.out.println("orderItem"+ currOrderItem);

            if (!currOrderItem.get().getOrder().getStatus().equals("PENDING") || orderItem.getQuantity() != 0) {
                  throw new IllegalArgumentException("Order is not pending or quantity is not 0");
            }

            orderItemRepository.deleteByItemItemId(orderItem.getItem().getItemId());
            orderItemRepository.save(orderItem);

            return currOrderItem.get();

      }
}
