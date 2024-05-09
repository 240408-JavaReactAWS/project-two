package com.revature.nile.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.val;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Table(name = "orderItems")

public class OrderItem {
    
    @Id
    @Column(name ="orderItemId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    //An order item is associated with one item
    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    private int quantity;

    //Many order items are associated with one order
    @ManyToOne
    @JoinColumn(name = "orderId")  
    @JsonIgnore
    private Order order;
}
