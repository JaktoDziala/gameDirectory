package com.gameDirectory.studio;

import com.gameDirectory.exceptions.InvalidDTOValueException;
import com.gameDirectory.exceptions.ObjectNotFoundException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudioService {

    private final StudioRepository studioRepository;

    StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public Studio addStudio(StudioDTO studioDTO) {
        validateDTO(studioDTO);
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

    void updateStudio(int id, StudioDTO studio) {
        Studio oldStudio = getStudio(id);
        if (studio.name() != null)
            oldStudio.setName(studio.name());
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

    void validateDTO(StudioDTO studioDTO){
        if (StringUtils.isBlank(studioDTO.name())){
            throw new InvalidDTOValueException("Name of studioDTO must not be blank!");
        }
        if (StringUtils.isBlank(studioDTO.description())){
            throw new InvalidDTOValueException("Description of studioDTO must not be blank!");
        }
    }
}
