package com.game.gameDirectory.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void getGame() throws Exception {
        mockMvc.perform(get("/v1/game/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    void addGame() {

    }

    @Test
    void getGames() throws Exception {
    }

    @Test
    void deleteGame() throws Exception {
    }

    @Test
    void deleteAll() throws Exception {
    }

    @Test
    void updateGame() throws Exception {
    }

    @Test
    void patchGameRating() throws Exception {
    }
}
