package com.game.gameDirectory.game;

import com.game.gameDirectory.game.enums.Genre;
import com.game.gameDirectory.game.enums.Platform;
import com.game.gameDirectory.studio.StudioDTO;
import com.game.gameDirectory.studio.StudioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@Import({GameService.class, StudioService.class})
public class GameServiceIntegrationTest {

    @Autowired
    GameService sut;
    @Autowired
    StudioService studioService;

    @Test
    @Transactional
    void assignToStudio_withValidIdsAndTransactionalAnnotation_persistsData() {
        studioService.addStudio(new StudioDTO(
                "description",
                List.of()
        ));

        sut.addGame(new GameDTO(
                "x", "x", "5-2-2000", "PC", "ACTION", null)
        );

        sut.assignToStudio(1, 1);
    }
}
