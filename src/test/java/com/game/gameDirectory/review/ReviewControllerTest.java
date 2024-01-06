package com.game.gameDirectory.review;

import com.game.gameDirectory.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void addReview_withValidObject_returnsStatusCode201() throws Exception{
        ReviewDTO review = new ReviewDTO(validId, validComment, validRating); // Create a ReviewDTO with necessary data
        mockMvc.perform(post("/v1/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(review)))
                .andExpect(status().isCreated());
    }

    @Test
    void getReview_withValidId_returnsStatusCode200() throws Exception {
        Review expectedReview = new Review();
        expectedReview.setId(validId);
        mockMvc.perform(get("/v1/review/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    void getReviews_returnsStatusCode200() throws Exception {
        mockMvc.perform(get("/v1/review/all"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview_withValidId_returnsStatusCode204() throws Exception {
        mockMvc.perform(delete("/v1/review/" + validId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReviews_withValidId_returnsStatusCode204() throws Exception {
        mockMvc.perform(delete("/v1/review/all"))
                .andExpect(status().isNoContent());
    }
}