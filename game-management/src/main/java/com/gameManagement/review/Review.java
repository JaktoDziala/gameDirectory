package com.gameManagement.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameManagement.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
