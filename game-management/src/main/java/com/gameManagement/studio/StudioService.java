package com.gameManagement.studio;

import com.gameManagement.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudioService {

    private final StudioRepository studioRepository;

    StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public Studio addStudio(StudioDTO studioDTO) {
        Studio studio = new Studio(
                studioDTO.name(),
                studioDTO.description(),
                studioDTO.games()
        );

        studioRepository.save(studio);
        return studio;
    }

    public Studio getStudio(int id) {
        return studioRepository.findById(id).
                orElseThrow(() -> new ObjectNotFoundException("Studio with Id " + id + " was not found"));
    }

    List<Studio> getStudios() {
        return studioRepository.findAll();
    }

    Studio updateStudio(int id, StudioDTO studio) {
        Studio oldStudio = getStudio(id);
        if (studio.name() != null)
            oldStudio.setName(studio.name());
        if (studio.description() != null)
            oldStudio.setDescription(studio.description());
        if (studio.games() != null)
            oldStudio.setGames(studio.games());

        return studioRepository.save(oldStudio);
    }

    void deleteStudio(int id) {
        studioRepository.deleteById(id);
    }

    void deleteStudios() {
        studioRepository.deleteAll();
    }
}
