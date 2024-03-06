package com.gameManagement.studio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameManagement.game.Game;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Studio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Game> games = new HashSet<>();

    public Studio(String name, String description, Set<Game> games) {
        this.name = name;
        this.description = description;
        this.games = games;
    }

    public Set<Game> getGames() {
        return games == null ? Collections.emptySet() : Collections.unmodifiableSet(games);
    }
}
