package com.gameManagement.game;

import com.gameManagement.annotation.ExampleOnly;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("v1/game/")
class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/add")
    ResponseEntity<Game> addGame(@Valid @RequestBody GameDTO gameDTO){
        return new ResponseEntity<>(gameService.addGame(gameDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{gameId}")
    ResponseEntity<Game> getGame(@PathVariable Integer gameId){
        return new ResponseEntity<>(gameService.getGame(gameId), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<Game>> getGames(){
        return new ResponseEntity<>(gameService.getGames(), HttpStatus.OK);
    }

    @DeleteMapping("/{gameId}")
    ResponseEntity<HttpStatus> deleteGame(@PathVariable Integer gameId){
        gameService.deleteGame(gameId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    ResponseEntity<HttpStatus> deleteAll(){
        gameService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{gameId}")
    ResponseEntity<Game> updateGame(@PathVariable int gameId, @RequestBody GameDTO gameDTO){
        return new ResponseEntity<>(
                gameService.updateGame(gameId, gameDTO),
                HttpStatus.OK
        );
    }

    @ExampleOnly
    @PatchMapping("/{gameId}")
    ResponseEntity<Game> patchGame(@PathVariable int gameId, @RequestBody GameDTO gameDTO){
        return new ResponseEntity<>(
                gameService.patchGame(gameId, gameDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/rating/{gameId}")
    ResponseEntity<Float> patchGameRating(@PathVariable int gameId, @RequestParam int rating){
        return new ResponseEntity<>(gameService.patchRating(gameId, rating), HttpStatus.OK);
    }

    // TODO: Reiterate Put vs Patch mapping. Unit/Int test actual difference
    @PatchMapping("/assign/studio")
    ResponseEntity<String> patchGameStudioTransactional(@RequestParam int gameId, @RequestParam int studioId){
        return new ResponseEntity<>(gameService.assignToStudio(gameId, studioId), HttpStatus.OK);
    }

    /**
     * Used only for presentation purposes of non-transactional behaviour.
     * For more information, see {@link GameService#assignToStudioNoTransaction(int, int)} method.
     */
    @ExampleOnly
    @Deprecated
    @PatchMapping("/assign/studio/non-transactional")
    ResponseEntity<String> patchGameStudioNonTransactional(@RequestParam int gameId, @RequestParam int studioId){
        return new ResponseEntity<>(gameService.assignToStudioNoTransaction(gameId, studioId), HttpStatus.OK);
    }
}
