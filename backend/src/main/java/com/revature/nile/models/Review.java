package com.revature.nile.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private LocalDateTime datePosted;

    // @JoinColumn(name = "fk_user_id")
    @JoinColumn(name = "userId")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    // @JoinColumn(name = "fk_item_id")
    @JoinColumn(name = "itemId")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Item item;
}
