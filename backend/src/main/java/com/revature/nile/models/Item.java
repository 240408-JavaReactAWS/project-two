package com.revature.nile.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {

    @Id
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
    private int quantity;

    @Column(nullable = false)
    private String image;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime datePosted;

    @Column
    private Double rating;

    public Item(Double rating, LocalDateTime datePosted, String image, int quantity, Double price, String description, String name, User sellerId, int itemId) {
        this.rating = rating;
        this.datePosted = datePosted;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.name = name;
        this.sellerId = sellerId;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item items = (Item) o;
        return itemId == items.itemId && sellerId == items.sellerId && quantity == items.quantity && Objects.equals(name, items.name) && Objects.equals(description, items.description) && Objects.equals(price, items.price) && Objects.equals(image, items.image) && Objects.equals(datePosted, items.datePosted) && Objects.equals(rating, items.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, sellerId, name, description, price, quantity, image, datePosted, rating);
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", sellerId=" + sellerId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                ", datePosted=" + datePosted +
                ", rating=" + rating +
                '}';
    }
}