package com.revature.nile.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private int reviewId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "text")
    private String text;

    @Column(name = "datePosted")
    @CreationTimestamp
    private Date datePosted;

    // A review is written by a user
    @JoinColumn(name = "userId")
     @ManyToOne(cascade = CascadeType.ALL)
     private User user;

    // A review is written for an item
    @JoinColumn(name = "itemId")
    @ManyToOne(cascade = CascadeType.ALL)
    private Item item;
}
