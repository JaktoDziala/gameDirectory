package com.game.gameDirectory.studio;

import com.game.gameDirectory.game.GameRepository;
import com.game.gameDirectory.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudioController.class)
class StudioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudioService studioService;
    @MockBean
    GameRepository gameRepository;

    final String validDescription = "ok";
    final int validId = 1;

    private StudioDTO studioDTO;

    @BeforeEach
    void setUp(){
        studioDTO = new StudioDTO(validDescription, List.of());
    }

    @Test
    void addGame() throws Exception {
        mockMvc.perform(post("/v1/studio/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(studioDTO)))
                .andExpect(status().isCreated());

        verify(studioService).addStudio(studioDTO);
    }

    @Test
    void getStudio() throws Exception {
        Studio studio = new Studio();
        studio.setDescription(validDescription);
        when(studioService.getStudio(eq(1))).thenReturn(studio);
        mockMvc.perform(get("/v1/studio/" + validId))
                .andExpect(status().isOk())
                // TODO: FixME
                .andExpect(jsonPath("$.description", is(studio.getDescription())));
    }

    @Test
    void getStudios() throws Exception {
        mockMvc.perform(get("/v1/studio/all"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudio() throws Exception {
        mockMvc.perform(put("/v1/studio/" + validId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.marshalJson(
                        new StudioDTO(validDescription, List.of())
                )))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudio() throws Exception {
    mockMvc.perform(delete("/v1/studio/" + validId))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudios() throws Exception {
        mockMvc.perform(delete("/v1/studio/all"))
                .andExpect(status().isNoContent());
    }
}