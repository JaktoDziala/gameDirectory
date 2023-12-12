package com.game.gameDirectory.review;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;

record ReviewDTO(Integer gameId, String comment, Integer rating) {
    ReviewDTO(@JsonProperty("gameId") Integer gameId,
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
