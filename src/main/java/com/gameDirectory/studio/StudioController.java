package com.gameDirectory.studio;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/studio")
public class StudioController {

    private final StudioService studioService;

    public StudioController(StudioService studioService) { this.studioService = studioService; }

    @PostMapping("/add")
    ResponseEntity<Studio> addStudio(@Valid @RequestBody StudioDTO studioDTO) {
        return new ResponseEntity<>(studioService.addStudio(studioDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Studio> getStudio(@PathVariable int id) {
        return new ResponseEntity<>(studioService.getStudio(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<Studio>> getStudios() {
        return new ResponseEntity<>(studioService.getStudios(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Studio> updateStudio(@PathVariable int id, @RequestBody StudioDTO studioDTO) {
        return new ResponseEntity<>(studioService.updateStudio(id, studioDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteStudio(@PathVariable int id) {
        studioService.deleteStudio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    ResponseEntity<HttpStatus> deleteStudios() {
        studioService.deleteStudios();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
