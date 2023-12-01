package com.game.gameDirectory.review;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO JSON CREATOR && JSON PROPERTY
    // TODO: Add Anki, when to use JsonCreator and JsonProperty
class ReviewDTO {
    private final Integer gameId;
    private final String comment;
    private final Integer rating;

    @JsonCreator
    public ReviewDTO(@JsonProperty("gameId") Integer gameId,
                     @JsonProperty("comment") String comment,
                     @JsonProperty("rating") Integer rating) {
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
