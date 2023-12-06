package com.game.gameDirectory.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ReviewController sut;
    @Mock
    private ReviewService reviewService;

    final int validId = 1;
    final String validComment = "2/10";
    final int validRating = 2;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    void getReviews() throws Exception {
        List<Review> reviews = new ArrayList<>();
        when(reviewService.getReviews()).thenReturn(reviews);

        mockMvc.perform(get("/v1/review/all"))
                .andExpect(status().isOk());

    }

    @Test
    void addReview() throws Exception{
        ReviewDTO review = new ReviewDTO(validId, validComment, validRating); // Create a ReviewDTO with necessary data
        String reviewJson = new ObjectMapper().writeValueAsString(review);

        mockMvc.perform(post("/v1/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isCreated());
    }



    @Test
    void getReview() throws Exception {
        Review expectedReview = new Review();
        expectedReview.setId(1);
        when(reviewService.getReview(validId)).thenReturn(expectedReview);

        mockMvc.perform(get("/v1/review/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview() throws Exception {
        mockMvc.perform(delete("/v1/review/delete/" + validId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReviews() throws Exception {
        mockMvc.perform(delete("/v1/review/delete/all"))
                .andExpect(status().isNoContent());
    }
}