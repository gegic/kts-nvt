package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingMainPhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingPhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/cultural-offerings", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingsController {

    private CulturalOfferingService culturalOfferingService;
    private CulturalOfferingMainPhotoService photoService;

    @Autowired
    public CulturalOfferingsController(CulturalOfferingService culturalOfferingService,
                                       CulturalOfferingMainPhotoService photoService) {
        this.culturalOfferingService = culturalOfferingService;
        this.photoService = photoService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOfferingDto>> getAll
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "3") int size,
             @RequestParam(defaultValue = "id,asc") String[] sort,
             @RequestParam(defaultValue = "", name = "search") String searchQuery,
             @RequestParam(defaultValue = "1f", name = "rating-min") float ratingMin,
             @RequestParam(defaultValue = "5f", name = "rating-max") float ratingMax,
             @RequestParam(defaultValue = "true", name = "no-reviews") boolean noReviews,
             @RequestParam(defaultValue = "-1", name = "category") long categoryId,
             @RequestParam(defaultValue = "-1", name = "subcategory") long subcategoryId,
             @RequestParam(defaultValue = "-180", name = "lng-start") float longitudeStart,
             @RequestParam(defaultValue = "180", name = "lng-end") float longitudeEnd,
             @RequestParam(defaultValue = "-90", name = "lat-start") float latitudeStart,
             @RequestParam(defaultValue = "90", name = "lat-end") float latitudeEnd,
             @RequestParam(defaultValue = "-1", name = "user") long userId) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.culturalOfferingService.readAll(p,
                searchQuery, ratingMin, ratingMax, noReviews, categoryId, subcategoryId,
                latitudeStart, latitudeEnd, longitudeStart, longitudeEnd, userId));
    }

    @GetMapping(path = "/bounds")
    public ResponseEntity<List<CulturalOfferingDto>> getByBounds (
            @RequestParam(name = "lng-start") float longitudeStart,
            @RequestParam(name = "lng-end") float longitudeEnd,
            @RequestParam(name = "lat-start") float latitudeStart,
            @RequestParam(name = "lat-end") float latitudeEnd,
            @RequestParam(defaultValue = "-1", name = "user") long userId) {

        return ResponseEntity.ok(this.culturalOfferingService
                .findByBounds(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd, userId));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CulturalOfferingDto> get(@PathVariable String id,
                                                   @RequestParam(defaultValue = "-1", name = "user") long userId){
        return ResponseEntity.of(this.culturalOfferingService.readById(Long.parseLong(id), userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/subscribe/cultural-offering/{culturalOfferingId}/user/{userId}")
    public ResponseEntity<CulturalOfferingDto> subscribe(@PathVariable long culturalOfferingId,
                                                         @PathVariable long userId) {
        return ResponseEntity.ok(this.culturalOfferingService.subscribe(culturalOfferingId, userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/unsubscribe/cultural-offering/{culturalOfferingId}/user/{userId}")
    public ResponseEntity<CulturalOfferingDto> unsubscribe(@PathVariable long culturalOfferingId,
                                                           @PathVariable long userId) {
        return ResponseEntity.ok(this.culturalOfferingService.unsubscribe(culturalOfferingId, userId));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping
    ResponseEntity<CulturalOfferingDto> add(@Valid @RequestBody CulturalOfferingDto culturalOfferingDto){
        CulturalOfferingDto saved = this.culturalOfferingService.create(culturalOfferingDto);
        return ResponseEntity.created(URI.create("/api/cultural-offering/" + saved.getId())).body(saved);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping(path="add-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CulturalOfferingPhotoDto> setPhoto(@RequestParam("photo") MultipartFile photoFile,
                               HttpServletRequest request){
        CulturalOfferingPhotoDto saved = this.photoService.addPhoto(photoFile);
        return ResponseEntity.created(URI.create(saved.getId().toString())).body(saved);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping
    ResponseEntity<CulturalOfferingDto> update(@Valid @RequestBody CulturalOfferingDto culturalOfferingDto){
        return ResponseEntity.ok(this.culturalOfferingService.update(culturalOfferingDto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.culturalOfferingService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/clear-photos")
    ResponseEntity<Void> deletePhotos(){
        this.photoService.clearPhotos();
        return ResponseEntity.ok().build();
    }
}
