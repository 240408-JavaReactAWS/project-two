package com.revature.nile.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Table(name = "orderItems")

public class OrderItem {
    
    @Id
    private int orderItemId;

    //An order item is associated with one item
    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    private int quantity;

    //An order item is associated with one order
    @ManyToOne
    @JoinColumn(name = "orderId")  
    private Order order;
}
