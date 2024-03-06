package com.gameManagement.game;

import com.gameManagement.annotation.ExampleOnly;
import com.gameManagement.studio.Studio;
import com.gameManagement.studio.StudioDTO;
import com.gameManagement.studio.StudioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({GameService.class, StudioService.class})
public class GameServiceIntegrationTest {

    @Autowired
    private GameService sut;
    @Autowired
    private StudioService studioService;
    private static final String validName = "studio z";
    private static final String validDescription = "description z";

    private static int dbIndexStartNumber = 1;

    @Test
    void assignToStudio_withValidIdsAndTransactionalAnnotation_persistsData() {
        // given
        Studio studio = studioService.addStudio(new StudioDTO(
                validName,
                "Transactional studio",
                Set.of()
        ));
        Game game = sut.addGame(new GameDTO(
                "Transactional name", "x", "2000-02-06", "PC", "ACTION", null)
        );

        // when
        sut.assignToStudio(dbIndexStartNumber, dbIndexStartNumber);

        // then
        assertEquals(sut.getGame(dbIndexStartNumber).getStudio(), studio);
        assertEquals(studioService.getStudio(dbIndexStartNumber).getGames().stream().findFirst(), Optional.of(game));

        // Combats indexing not resetting on database clear after each test. FIXME
        dbIndexStartNumber++;
    }

    @ExampleOnly
    @Test
    void assignToStudio_withValidIdsAndNotTransactionalAnnotation_doesNotPersistsData() {
        // given
        Studio studio = studioService.addStudio(new StudioDTO(
                validName,
                "Non-transactional studio",
                Set.of()
        ));
        Game game = sut.addGame(new GameDTO(
                "Non-transactional name", "x", "2000-02-06", "PC", "ACTION", null)
        );

        // when
        sut.assignToStudioNoTransaction(dbIndexStartNumber, dbIndexStartNumber);

        // then
        assertEquals(sut.getGame(dbIndexStartNumber).getStudio(), studio);
        assertEquals(studioService.getStudio(dbIndexStartNumber).getGames().stream().findFirst(), Optional.of(game));
    }
}
