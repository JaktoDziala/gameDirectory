package com.game.gameDirectory.studio;

import com.game.gameDirectory.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class StudioService {

    private final StudioRepository studioRepository;

    StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    Studio addStudio(StudioDTO studioDTO) {
        Studio studio = new Studio(
                studioDTO.description(),
                studioDTO.games()
        );

        studioRepository.save(studio);
        return studio;
    }

    Studio getStudio(int id) {
        return studioRepository.findById(id).
                orElseThrow(() -> new ObjectNotFoundException("Studio with Id " + id + " was not found"));
    }

    List<Studio> getStudios() {
        return studioRepository.findAll();
    }

    void updateStudio(int id, StudioDTO studio) {
        Studio oldStudio = getStudio(id);
        if (studio.description() != null)
            oldStudio.setDescription(studio.description());
        if (studio.games() != null)
            oldStudio.setGames(studio.games());

        studioRepository.save(oldStudio);
    }

    void deleteStudio(int id) {
        studioRepository.deleteById(id);
    }

    void deleteStudios() {
        studioRepository.deleteAll();
    }
}
