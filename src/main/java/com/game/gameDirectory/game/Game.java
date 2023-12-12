package com.game.gameDirectory.game;

import com.game.gameDirectory.platform.Platform;
import com.game.gameDirectory.review.Review;
import com.game.gameDirectory.studio.Studio;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
    private Date releaseDate;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    private Float rating = 0.f;
    private Integer reviewCount = 0;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    @ManyToOne
    private Studio studio;

    public Game(String title, String description, Date releaseDate, Platform platform, Studio studio) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.platform = platform;
        this.studio = studio;
    }

    public List<Review> getReviews(){
        return Collections.unmodifiableList(reviews);
    }
}
