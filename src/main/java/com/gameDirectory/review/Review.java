package com.gameDirectory.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameDirectory.game.Game;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
    @Nonnull
    @JsonIgnore
    private Game game;
    @NonNull
    private String comment;
    @Nonnull
    private Integer rating;

    public Review(Game game, String comment, Integer rating) {
        this.game = game;
        this.comment = comment;
        this.rating = rating;
    }
}
