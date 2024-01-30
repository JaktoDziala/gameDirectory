package com.game.gameDirectory.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/review")
class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    // TODO: Read if DTO validation should be handled on DTO level or at service level
    // TODO: Refactor, return created content
    // TODO: Read when it's okay to return unchecked exception. It's thrown for validating ReviewDTO
    @PostMapping("/add")
    ResponseEntity<HttpStatus> addReview(@RequestBody ReviewDTO review) {
        reviewService.addReview(review);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Review> getReview(@PathVariable int id){
        return new ResponseEntity<>(reviewService.getReview(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<Review>> getReviews(){
        return new ResponseEntity<>(reviewService.getReviews(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteReview(@PathVariable int id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    ResponseEntity<HttpStatus> deleteReviews(){
        reviewService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
