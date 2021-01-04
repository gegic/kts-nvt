package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.ReviewPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewPhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@PreAuthorize("hasRole('MODERATOR') || hasRole('USER')")
@RestController
@RequestMapping(path = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewsController {

    private final ReviewService reviewService;
    private final ReviewPhotoService photoService;

    @Autowired
    public ReviewsController(ReviewService reviewService,
                             ReviewPhotoService photoService) {
        this.reviewService = reviewService;
        this.photoService = photoService;
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

    @GetMapping(path="/by-rating/cultural-offering/{culturalOfferingId}")
    public ResponseEntity<List<ReviewNumbersDto>> groupByRating(@PathVariable long culturalOfferingId) {
        List<ReviewNumbersDto> reviewNumbers = reviewService.findAndGroupByRating(culturalOfferingId);
        return ResponseEntity.ok(reviewNumbers);
    }

    @GetMapping(path="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.reviewService.readById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(path="/cultural-offering/{culturalOfferingId}/user/{userId}")
    ResponseEntity<ReviewDto> getForUser(@PathVariable long culturalOfferingId,
                                         @PathVariable long userId) {
        return ResponseEntity.of(this.reviewService.findByCulturalOfferingAndUser(culturalOfferingId, userId));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(path="/photos/id/{id}")
    ResponseEntity<Void> deletePhotosForReview(@PathVariable long id) {
        photoService.deleteForReview(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    ResponseEntity<ReviewDto> add(@Valid @RequestBody ReviewDto reviewDto){
        ReviewDto saved = this.reviewService.create(reviewDto);
        return ResponseEntity.created(URI.create(String.format("/api/review/%s", saved.getId())))
                .body(saved);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping
    ResponseEntity<ReviewDto> update(@Valid @RequestBody ReviewDto reviewDto){
        return ResponseEntity.ok(this.reviewService.update(reviewDto));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="/add-photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<List<ReviewPhotoDto>> setPhoto(@RequestParam("photos") MultipartFile[] photoFiles){
        List<ReviewPhotoDto> saved = this.photoService.addPhotos(photoFiles);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id){
        this.reviewService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear-photos")
    ResponseEntity<Void> delete(){
        this.photoService.clearPhotos();
        return ResponseEntity.ok().build();
    }

}
