package com.gameDirectory.review;

import com.gameDirectory.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @InjectMocks
    private ReviewController sut;
    @Mock
    private ReviewService reviewService;
    private MockMvc mockMvc;
    private final static int validId = 1;
    private final static String validComment = "2/10";
    private final static int validRating = 2;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    void addReview_withValidDTO_returnsStatusCode201() throws Exception {
        ReviewDTO review = new ReviewDTO(validId, validComment, validRating);
        mockMvc.perform(post("/v1/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(review)))
                .andExpect(status().isCreated());
    }


    public static Stream<ReviewDTO> notValidDTOs() {
        return Stream.of(
                new ReviewDTO(null, null, null),
                new ReviewDTO(validId, null, null),
                new ReviewDTO(validId, validComment, null)
        );
    }

    @MethodSource("notValidDTOs")
    @ParameterizedTest
    void addReview_withNotValidDTOValues_returnsStatusCode400(ReviewDTO reviewDTO) throws Exception {
        mockMvc.perform(post("/v1/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(reviewDTO)))
                .andExpect(status().isBadRequest());
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