package com.game.gameDirectory.game;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/game/")
class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("add")
    ResponseEntity<Game> addGame(@RequestBody GameDTO gameDTO) throws Exception{
        return new ResponseEntity<>(gameService.addGame(gameDTO), HttpStatus.CREATED);
    }

    @GetMapping("{gameId}")
    ResponseEntity<Game> getGame(@PathVariable Integer gameId){
        return new ResponseEntity<>(gameService.getGame(gameId), HttpStatus.OK);
    }

    @GetMapping("all")
    ResponseEntity<List<Game>> getGames(){
        return new ResponseEntity<>(gameService.getGames(), HttpStatus.OK);
    }

    @DeleteMapping("{gameId}")
    ResponseEntity<HttpStatus> deleteGame(@PathVariable Integer gameId){
        gameService.deleteGame(gameId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("all")
    ResponseEntity<HttpStatus> deleteAll(){
        gameService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{gameId}")
    ResponseEntity<Game> updateGame(@PathVariable Integer gameId, @RequestBody Game game){
        gameService.updateGame(gameId, game);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PatchMapping("rating/{gameId}")
    ResponseEntity<Float> patchGameRating(@PathVariable int gameId, @RequestParam int rating){
        return new ResponseEntity<>(gameService.patchRating(gameId, rating), HttpStatus.OK);
    }
}
