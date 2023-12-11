package com.game.gameDirectory.game;
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

    @PostMapping("add")
    ResponseEntity<Game> addGame(@RequestBody Game game) throws Exception{
        gameService.addGame(game);
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    }

    @GetMapping("{gameId}")
    ResponseEntity<Game> getGame(@PathVariable Integer gameId){
        return new ResponseEntity<>(gameService.getGame(gameId), HttpStatus.OK);
    }

    @GetMapping("all")
    ResponseEntity<List<Game>> getGames(){
        return new ResponseEntity<>(gameService.getGames(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{gameId}")
    ResponseEntity<HttpStatus> deleteGame(@PathVariable Integer gameId){
        gameService.deleteGame(gameId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/all")
    ResponseEntity<HttpStatus> deleteAll(){
        gameService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{gameId}")
    ResponseEntity<Game> updateGame(@PathVariable Integer gameId, @RequestBody Game game){
        gameService.updateGame(gameId, game);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PatchMapping("/patch/rating/{gameId}")
    ResponseEntity<Float> patchGameRating(@PathVariable int gameId, @RequestParam int rating){
        return new ResponseEntity<>(gameService.patchRating(gameId, rating), HttpStatus.OK);
    }


}
