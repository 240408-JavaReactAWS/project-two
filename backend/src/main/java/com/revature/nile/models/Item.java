package com.revature.nile.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    private User sellerId;

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

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime datePosted;

    @Column
    private Double rating;

    public Item(Double rating, LocalDateTime datePosted, String image, int stock, Double price, String description, String name, User sellerId, int itemId) {
        this.rating = rating;
        this.datePosted = datePosted;
        this.image = image;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.name = name;
        this.sellerId = sellerId;
        this.itemId = itemId;
    }
}