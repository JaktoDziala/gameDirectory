package com.gameDirectory.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameDirectory.game.Game;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Game game;
    private String comment;
    private Integer rating;

    public Review(Game game, String comment, Integer rating) {
        this.game = game;
        this.comment = comment;
        this.rating = rating;
    }
}
