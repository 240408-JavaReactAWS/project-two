package com.revature.nile.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Table(name = "users")
public class User {

    @Id
    @Column(name ="userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    //A user can have multiple orders (but only one pending at a given time!)
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    //A user can have multiple reviews
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    //A user can have multiple items for sale
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Item> items;

    public User(String email, String userName, String password, String address, String firstName, String lastName) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}