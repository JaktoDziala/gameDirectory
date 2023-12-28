package com.game.gameDirectory.studio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/studio")
public class StudioController {

    private final StudioService studioService;

    public StudioController(StudioService studioService) { this.studioService = studioService; }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("add")
    void addGame(@RequestBody StudioDTO studioDTO) {
        studioService.addStudio(studioDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    void getStudio(@PathVariable int id) {
        studioService.getStudio(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("all")
    void getStudios() {
        studioService.getStudios();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    void updateStudio(@PathVariable int id, @RequestBody StudioDTO studioDTO) {
        studioService.updateStudio(id, studioDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    void deleteStudio(@PathVariable int id) {
        studioService.deleteStudio(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("all")
    void deleteStudios() {
        studioService.deleteStudios();
    }
}
