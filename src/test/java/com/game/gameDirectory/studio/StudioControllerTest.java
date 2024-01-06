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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudioController.class)
class StudioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudioService studioService;

    // Needed for temporary Exception handler heresy
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
    void addStudio_withValidDTO_returns201StatusCode() throws Exception {
        Studio studio = new Studio(validDescription, List.of());
        when(studioService.addStudio(studioDTO)).thenReturn(studio);
        mockMvc.perform(post("/v1/studio/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(studioDTO)))
                .andExpect(jsonPath("$.description", is(validDescription)))
                .andExpect(jsonPath("$.games").doesNotExist())
                .andExpect(status().isCreated());

        verify(studioService).addStudio(studioDTO);
    }

    @Test
    void getStudio_withValidId_returns200StatusCode() throws Exception {
        Studio studio = new Studio();
        studio.setDescription(validDescription);
        when(studioService.getStudio(eq(1))).thenReturn(studio);
        mockMvc.perform(get("/v1/studio/" + validId))
                .andExpect(jsonPath("$.description", is(validDescription)))
                .andExpect(jsonPath("$.games").doesNotExist())
                .andExpect(status().isOk()).andReturn().getResponse();

    }

    // TODO: FiXMe
    @Test
    void getStudios_returns200StatusCode() throws Exception {
        Studio studio = new Studio();
        studio.setId(1);
        studio.setDescription(validDescription);
        when(studioService.getStudios()).thenReturn(List.of(studio));
        mockMvc.perform(get("/v1/studio/all"))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is(validDescription)))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudio_withValidDTO_returns200StatusCode() throws Exception {
        mockMvc.perform(put("/v1/studio/" + validId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.marshalJson(
                        new StudioDTO(validDescription, List.of())
                )))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudio_withValidId_returns204StatusCode() throws Exception {
    mockMvc.perform(delete("/v1/studio/" + validId))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudios_returns204StatusCode() throws Exception {
        mockMvc.perform(delete("/v1/studio/all"))
                .andExpect(status().isNoContent());
    }
}