package com.revature.nile.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int sellerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private Double price;

    private int quantity;

    @Column(nullable = false)
    private String image;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime datePosted;
    
    private Double rating;
    
    public Item(){
        
    }

    public Item(Double rating, LocalDateTime datePosted, String image, int quantity, Double price, String description, String name, int sellerId, int id) {
        this.rating = rating;
        this.datePosted = datePosted;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.name = name;
        this.sellerId = sellerId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item items = (Item) o;
        return id == items.id && sellerId == items.sellerId && quantity == items.quantity && Objects.equals(name, items.name) && Objects.equals(description, items.description) && Objects.equals(price, items.price) && Objects.equals(image, items.image) && Objects.equals(datePosted, items.datePosted) && Objects.equals(rating, items.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sellerId, name, description, price, quantity, image, datePosted, rating);
    }
}
