package com.game.gameDirectory.game;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import com.game.gameDirectory.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {

    @InjectMocks
    GameController sut;
    @MockBean
    GameService gameService;
    @MockBean
    GameRepository gameRepository;

    @Autowired
    private MockMvc mockMvc;

    private final int validId = 1;

    @Test
    void addGame_withValidObject_returnsStatusCode201() throws Exception {
        mockMvc.perform(post("/v1/game/add")
                        .content(JsonUtil.marshalJson(new Game()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
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
