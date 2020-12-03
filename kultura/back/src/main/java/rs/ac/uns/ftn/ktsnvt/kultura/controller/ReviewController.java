package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/review", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReviewDto>> get(@PathVariable long culturalOfferingId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "3") int size,
                                              @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<ReviewDto> reviewDtos = this.reviewService
                .readAllByCulturalOfferingId(culturalOfferingId, p);
        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.reviewService.readById(id));
    }

    @PostMapping
    ResponseEntity<ReviewDto> add(@RequestBody ReviewDto reviewDto){
        ReviewDto saved = this.reviewService.save(reviewDto);
        return ResponseEntity.created(URI.create(String.format("/api/review/%s", saved.getId())))
                .body(saved);
    }

    @PutMapping
    ResponseEntity<ReviewDto> update(@RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(this.reviewService.save(reviewDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.reviewService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
