package com.game.gameDirectory.studio;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudioController {

    private final StudioService studioService;

    StudioController(StudioService studioService){
        this.studioService = studioService;
    }

    @ResponseStatus(HttpStatus.OK)
    void addGame(@RequestBody Studio studio){
        studioService.addStudio(studio);
    }

    void getStudio(){

    }

    void getStudios(){

    }

    void updateStudio(){

    }

    void deleteStudio(){

    }

    void deleteStudios(){

    }


}
