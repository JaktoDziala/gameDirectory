package com.gameDirectory.studio;

import com.gameDirectory.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudioServiceTest {

    @InjectMocks
    private StudioService sut;
    @Mock
    private StudioRepository studioRepository;
    private static final int validId = 1;
    private static final int invalidId = -1;
    private static final String validName = "valid name";
    private static final String validDescription = "2137";

    @Test
    void addStudio_withValidObject_addsStudio() {
        // given
        StudioDTO studioDTO = new StudioDTO(validName, validDescription, null);

        // when
        Studio result =  sut.addStudio(studioDTO);

        // then
        verify(studioRepository).save(Mockito.any(Studio.class));
        assertEquals(validDescription, result.getDescription());
        assertEquals(result.getGames(), new HashSet<>());
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
        Studio studio = new Studio(validName, validDescription, new HashSet<>());
        StudioDTO studioDTO = new StudioDTO(
                validName,
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
        Studio studio = new Studio(validName, validDescription, new HashSet<>());
        StudioDTO studioDTO = new StudioDTO(
                validName,
                null,
                new HashSet<>()
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
                validName,
                null,
                new HashSet<>()
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