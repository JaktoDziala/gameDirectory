package com.gameDirectory.game;

import com.gameDirectory.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    @InjectMocks
    private GameController sut;
    @MockBean
    private GameService gameService;
    @MockBean
    private GameRepository gameRepository;
    @Autowired
    private MockMvc mockMvc;

    private static final int validId = 1;
    private static final String validTitle = "title";
    private static final String validDescription = "description";
    private static final String validReleaseDate = "2000-02-06";
    private static final String validPlatform = "XBOX";
    private static final String validGenre = "ACTION";

    @Test
    void addGame_withValidDTO_returnsStatusCode201() throws Exception {
        mockMvc.perform(post("/v1/game/add")
                        .content(JsonUtil.marshalJson(new GameDTO("title", "description", "2000-04-04", "Not blank", "Not blank", validId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    public static Stream<GameDTO> notValidDTOs() {
        return Stream.of(
                new GameDTO(null, null, null, null, null, null),
                new GameDTO(validTitle, null, null, null, null, null),
                new GameDTO(validTitle, validDescription, null, null, null, null),
                new GameDTO(validTitle, validDescription, validReleaseDate, null, null, null),
                new GameDTO(validTitle, validDescription, validReleaseDate, validPlatform, null, null)
        );
    }

    @MethodSource("notValidDTOs")
    @ParameterizedTest
    void addGame_withNotValidRequiredDTOValues_returnsStatusCode400(GameDTO value) throws Exception {
        mockMvc.perform(post("/v1/game/add")
                        .content(JsonUtil.marshalJson(value))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getGame_withValidId_returnsStatusCode200() throws Exception {
        mockMvc.perform(get("/v1/game/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    void getGames_returnsStatusCode200() throws Exception {
        mockMvc.perform(get("/v1/game/all"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteGame_withValidId_returnsStatusCode204() throws Exception {
        mockMvc.perform(delete("/v1/game/" + validId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAll_withValidId_returnsStatusCode204() throws Exception {
        mockMvc.perform(delete("/v1/game/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateGame_withValidId_returnsStatusCode200() throws Exception {
        mockMvc.perform(put("/v1/game/" + validId)
                        .content(JsonUtil.marshalJson(new Game()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void patchGameRating_withValidId_returnsStatusCode200() throws Exception {
        final int validRating = 5;
        mockMvc.perform(patch("/v1/game/rating/{gameId}", validId)
                        .param("rating", String.valueOf(validRating)))
                .andExpect(status().isOk());
    }
}
