package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.OrderRepository;
import com.revature.nile.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderService {
      private final OrderRepository orderRepository;
      private final UserRepository userRepository;
      private final OrderItemRepository orderItemRepository;

      @Autowired
      public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
            this.orderRepository = orderRepository;
            this.userRepository = userRepository;
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

      public Order addOrderItemToCart(int userId, OrderItem orderItem) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if(optionalUser.isEmpty()) {
                  return null;
            }
            User user = optionalUser.get();
            Order currOrder = this.getCurrentOrderByUserId(userId);
            if (currOrder == null) {
                  Order newOrder = new Order();
                  newOrder.setUser(user);
                  newOrder.setStatus(Order.StatusEnum.PENDING);
                  newOrder.setBillAddress(user.getAddress());
                  newOrder.setShipToAddress(user.getAddress());
                  newOrder.setOrderItems(new ArrayList<OrderItem>());
                  this.createOrder(newOrder);
                  currOrder = newOrder;
            }
            orderItem.setOrder(currOrder);
            for(OrderItem cartItems : currOrder.getOrderItems()) { // if item id is already in cart, add quantity
                  if(cartItems.getItem().getItemId() == orderItem.getItem().getItemId()) {
                        cartItems.setQuantity(cartItems.getQuantity() + orderItem.getQuantity());
                        orderItemRepository.save(cartItems);
                        return currOrder;
                  }
            }
            this.createOrderItem(orderItem);
            currOrder.getOrderItems().add(orderItem);
            return currOrder;
      }
}
