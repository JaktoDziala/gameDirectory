package com.game.gameDirectory.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/review")
class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/add")
    ResponseEntity<HttpStatus> addReview(@RequestBody ReviewDTO review) {
        reviewService.addReview(review);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    ResponseEntity<Review> getReview(@PathVariable int id){
        return new ResponseEntity<>(reviewService.getReview(id), HttpStatus.OK);
    }

    @GetMapping("all")
    ResponseEntity<List<Review>> getReviews(){
        return new ResponseEntity<>(reviewService.getReviews(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    ResponseEntity<HttpStatus> deleteReview(@PathVariable int id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("all")
    ResponseEntity<HttpStatus> deleteReviews(){
        reviewService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
