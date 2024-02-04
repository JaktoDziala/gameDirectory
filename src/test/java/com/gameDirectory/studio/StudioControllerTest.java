package com.gameDirectory.studio;

import com.gameDirectory.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

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
    private MockMvc mockMvc;
    @MockBean
    StudioService studioService;

    private final static int validId = 1;
    private final static String validName = "name";
    private final static String validDescription = "description";

    private StudioDTO studioDTO;

    @BeforeEach
    void setUp(){
        studioDTO = new StudioDTO(validName, validDescription, List.of());
    }

    @Test
    void addStudio_withValidDTO_returnsStatusCode201() throws Exception {
        Studio studio = new Studio(validName, validDescription, List.of());
        when(studioService.addStudio(studioDTO)).thenReturn(studio);
        mockMvc.perform(post("/v1/studio/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(studioDTO)))
                .andExpect(jsonPath("$.description", is(validDescription)))
                .andExpect(jsonPath("$.games").doesNotExist())
                .andExpect(status().isCreated());

        verify(studioService).addStudio(studioDTO);
    }

    public static Stream<StudioDTO> notValidDTOs() {
        return Stream.of(
                new StudioDTO(null, null, null),
                new StudioDTO(validName, null, null)
        );
    }

    @MethodSource("notValidDTOs")
    @ParameterizedTest
    void addStudio_withNotValidDTOValues_returnsStatusCode400(StudioDTO studio) throws Exception {
        mockMvc.perform(post("/v1/studio/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.marshalJson(studio)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudio_withValidId_returnsStatusCode200() throws Exception {
        Studio studio = new Studio();
        studio.setDescription(validDescription);
        when(studioService.getStudio(eq(1))).thenReturn(studio);
        mockMvc.perform(get("/v1/studio/" + validId))
                .andExpect(jsonPath("$.description", is(validDescription)))
                .andExpect(jsonPath("$.games").doesNotExist())
                .andExpect(status().isOk()).andReturn().getResponse();

    }

    @Test
    void getStudios_returnsStatusCode200() throws Exception {
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
    void updateStudio_withValidDTO_returnsStatusCode200() throws Exception {
        mockMvc.perform(put("/v1/studio/" + validId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.marshalJson(
                        new StudioDTO(validName, validDescription, List.of())
                )))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudio_withValidId_returnsStatusCode204() throws Exception {
    mockMvc.perform(delete("/v1/studio/" + validId))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteStudios_returnsStatusCode204() throws Exception {
        mockMvc.perform(delete("/v1/studio/all"))
                .andExpect(status().isNoContent());
    }
}