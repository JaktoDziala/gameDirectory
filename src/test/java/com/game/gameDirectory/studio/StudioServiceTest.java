package com.game.gameDirectory.studio;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudioServiceTest {

    @InjectMocks
    private StudioService sut;
    @Mock
    private StudioRepository studioRepository;

    private final int validId = 1;
    private final int invalidId = -1;
    private final String validDescription = "2137";

    @Test
    void addStudio_withValidObject_addsStudio() {
        // given
        StudioDTO studioDTO = new StudioDTO(validDescription, null);

        // when
        Studio result =  sut.addStudio(studioDTO);

        // then
        verify(studioRepository).save(Mockito.any(Studio.class));
        assertEquals(validDescription, result.getDescription());
        assertNull(result.getGames());
    }

    @Test
    void getStudio_withValidId_returnsStudio() {
        // given
        Studio studio = new Studio();
        when(studioRepository.findById(validId)).thenReturn(Optional.of(studio));

        // when
        Studio result = sut.getStudio(validId);

        // then
        verify(studioRepository).findById(validId);
        assertEquals(studio, result);
    }

    @Test
    void getStudio_withNotValidId_throwsException() {
        // given
        // when
        assertThrows(ObjectNotFoundException.class, () -> sut.getStudio(invalidId));

        // then
        verify(studioRepository).findById(invalidId);
    }

    @Test
    void getStudios_returnsStudios() {
        // given
        Studio studio = new Studio();
        List<Studio> studios = new ArrayList<>(List.of(
                    studio
                ));
        when(studioRepository.findAll()).thenReturn(studios);

        // when
        List<Studio> result = sut.getStudios();

        // then
        verify(studioRepository).findAll();
        assertEquals(studios, result);
    }

    @Test
    void updateStudio_withValidDescriptionOnly_updatesOnlyDescription() {
        // given
        Studio studio = new Studio(validDescription, new ArrayList<>());
        StudioDTO studioDTO = new StudioDTO(
                "xxx",
                null
        );
        when(studioRepository.findById(validId)).thenReturn(Optional.of(studio));

        // when
        sut.updateStudio(validId, studioDTO);

        //then
        verify(studioRepository).save(studio);
        assertEquals(studioDTO.description(), studio.getDescription());
        assertNotEquals(studioDTO.games(), studio.getGames());
    }

    @Test
    void updateStudio_withValidGamesOnly_updatesOnlyGamesList() {
        // given
        Studio studio = new Studio(validDescription, new ArrayList<>());
        StudioDTO studioDTO = new StudioDTO(
                null,
                new ArrayList<>()
        );
        when(studioRepository.findById(validId)).thenReturn(Optional.of(studio));

        // when
        sut.updateStudio(validId, studioDTO);

        //then
        verify(studioRepository).save(studio);
        assertEquals(studioDTO.games(), studio.getGames());
        assertNotEquals(studioDTO.description(), studio.getDescription());
    }


    @Test
    void updateStudio_withNotValidId_throwsException() {
        // given
        StudioDTO studioDTO = new StudioDTO(
                null,
                new ArrayList<>()
        );

        // when
        // then
        assertThrows(ObjectNotFoundException.class, () -> sut.updateStudio(invalidId, studioDTO));
        verify(studioRepository, never()).save(Mockito.any());
        }


    @Test
    void deleteStudio_deletesStudio() {
        // given
        // when
        sut.deleteStudio(validId);
        // then
        verify(studioRepository).deleteById(validId);
    }

    @Test
    void deleteStudios_deleteStudios() {
        // given
        // when
        sut.deleteStudios();
        //then
        verify(studioRepository).deleteAll();
    }
}