package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.Set;
import java.util.TreeSet;


@RestController
@RequestMapping(path = "/api/review", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {

    private ReviewService reviewService;
    private ModelMapper modelMapper;

    @Autowired
    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ReviewDto>> get(@PathVariable String culturalOfferingId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size,
                                             @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<ReviewDto> reviewDtos = this.reviewService
                .readAllByCulturalOfferingId(Long.parseLong(culturalOfferingId), p)
                .map(review -> modelMapper.map(review, ReviewDto.class));
        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.reviewService.readById(id).map(r -> modelMapper.map(r, ReviewDto.class)));
    }

    @PostMapping
    ResponseEntity<ReviewDto> add(@RequestBody Review Review){
        Review saved = this.reviewService.save(Review);
        return ResponseEntity.created(URI.create(String.format("/api/review/%s", saved.getId())))
                .body(modelMapper.map(saved, ReviewDto.class));
    }

    @PutMapping
    ResponseEntity<ReviewDto> update(@RequestBody Review Review){
        return ResponseEntity.ok(modelMapper.map(this.reviewService.save(Review), ReviewDto.class));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id) {
        this.reviewService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }
}
