package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/review/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {

    private ReviewService reviewService;
    private ModelMapper modelMapper;

    @Autowired
    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Review>> get(@PathVariable String culturalOfferingId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.reviewService.readAllByCulturalOfferingId(UUID.fromString(culturalOfferingId), p));
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Review> getById(@PathVariable UUID id) {
        return ResponseEntity.of(this.reviewService.readById(id));
    }

    @PostMapping
    ResponseEntity<Review> add(@RequestBody Review Review){
        Review saved = this.reviewService.save(Review);
        return ResponseEntity.created(URI.create(String.format("/api/review/%s", saved.getId()))).body(saved);
    }

    @PutMapping
    ResponseEntity<Review> update(@RequestBody Review Review){
        return ResponseEntity.ok(this.reviewService.save(Review));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.reviewService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
