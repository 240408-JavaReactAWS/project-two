package com.revature.nile.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
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

    @OneToMany(mappedBy = "user")
    private List<Order> Orders;

    @OneToMany(mappedBy = "user")
    private List<Review> Reviews;

    @OneToMany(mappedBy = "user")
    private List<Item> Items;

    public User(String email, String userName, String password, String address, String firstName, String lastName) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}