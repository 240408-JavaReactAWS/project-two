package com.revature.nile.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Table(name = "items")
public class Item {

    @Id
    @Column(name ="itemId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    //An item is sold by a user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String image;

    @Column
    @CreationTimestamp
    private Date datePosted;

    @Column
    private Double rating;

    //An item can have many reviews
    @OneToMany(mappedBy = "item")
    private List<Review> reviews;

    //An item can be part of multiple order items
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;

    public Item(String image, int stock, Double price, String description, String name, User seller) {
        this.image = image;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.name = name;
        this.user = seller;
    }
}