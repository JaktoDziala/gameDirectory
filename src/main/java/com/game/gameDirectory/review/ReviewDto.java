package com.game.gameDirectory.review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ReviewDto {
    private final Integer gameId;
    private final String comment;
    private final Integer rating;

    public ReviewDto(Integer gameId, String comment, Integer rating) {
        if (gameId == null)
            throw new NullPointerException("gameId of review must not be null!");
        if (comment == null)
            throw new NullPointerException("comment of review must not be null!");
        if (rating == null)
            throw new NullPointerException("rating of review must not be null!");

        this.gameId = gameId;
        this.comment = comment;
        this.rating = rating;
    }
}
