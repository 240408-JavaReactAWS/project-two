package com.revature.nile.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name ="userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "user")
    private List<Object> Orders; // TODO replace with proper objects later

    @OneToMany(mappedBy = "user")
    private List<Object> Reviews; // TODO replace with proper objects later

    @OneToMany(mappedBy = "user")
    private List<Object> Items; // TODO replace with proper objects later

    public User() {

    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String email, String username, String password, String address, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Object> getOrders() {
        return Orders;
    }

    public void setOrders(List<Object> Orders) {
        this.Orders = Orders;
    }

    public List<Object> getReviews() {
        return Reviews;
    }

    public void setReviews(List<Object> Reviews) {
        this.Reviews = Reviews;
    }

    public List<Object> getItems() {
        return Items;
    }

    public void setItems(List<Object> Items) {
        this.Items = Items;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


}