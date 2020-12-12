package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import javax.validation.Valid;
import java.net.URI;

@PreAuthorize("hasRole('MODERATOR') || hasRole('USER')")
@RestController
@RequestMapping(path = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewsController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewsController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(path = "/cultural-offering/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReviewDto>> get(@PathVariable long culturalOfferingId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "3") int size,
                                              @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<ReviewDto> reviewDtos = this.reviewService
                .readAllByCulturalOfferingId(culturalOfferingId, p);
        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.reviewService.readById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    ResponseEntity<ReviewDto> add(@Valid @RequestBody ReviewDto reviewDto){
        ReviewDto saved = this.reviewService.save(reviewDto);
        return ResponseEntity.created(URI.create(String.format("/api/review/%s", saved.getId())))
                .body(saved);
    }

    @PutMapping
    ResponseEntity<ReviewDto> update(@Valid @RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(this.reviewService.save(reviewDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.reviewService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
