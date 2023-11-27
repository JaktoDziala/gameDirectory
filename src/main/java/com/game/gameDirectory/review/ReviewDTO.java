package com.game.gameDirectory.review;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ReviewDTO {
    private final Integer gameId;
    private final String comment;
    private final Integer rating;

    public ReviewDTO(Integer gameId, String comment, Integer rating) {
        if (gameId == null)
            throw new NullPointerException("gameId of review must not be null!");
        if (StringUtils.isBlank(comment))
            throw new NullPointerException("comment of review must not be null!");
        if (rating == null)
            throw new NullPointerException("rating of review must not be null!");

        this.gameId = gameId;
        this.comment = comment;
        this.rating = rating;
    }
}
