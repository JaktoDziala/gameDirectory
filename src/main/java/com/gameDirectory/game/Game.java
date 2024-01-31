package com.gameDirectory.game;

import com.gameDirectory.game.enums.Genre;
import com.gameDirectory.game.enums.Platform;
import com.gameDirectory.review.Review;
import com.gameDirectory.studio.Studio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private Float rating = 0.f;
    private Integer reviewCount = 0;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    @ManyToOne
    private Studio studio;

    public Game(String title, String description, LocalDate releaseDate, Platform platform, Studio studio, Genre genre) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.platform = platform;
        this.studio = studio;
        this.genre = genre;
    }

    // TODO: List vs Set for saving to DB
    public List<Review> getReviews(){
        return Collections.unmodifiableList(reviews);
    }
}
