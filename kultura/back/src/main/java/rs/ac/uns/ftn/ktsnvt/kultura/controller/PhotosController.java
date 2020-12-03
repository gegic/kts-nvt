package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/photos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhotosController {

    private final PhotoService photoService;

    @Autowired
    public PhotosController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PhotoDto>> get(@PathVariable long culturalOfferingId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "3") int size,
                                              @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<PhotoDto> photoDtos = this.photoService
                .readAllByCulturalOfferingId(culturalOfferingId, p);
        return ResponseEntity.ok(photoDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.photoService.readById(id));
    }

    @PostMapping
    ResponseEntity<PhotoDto> add(@RequestBody PhotoDto photoDto){
        PhotoDto saved = this.photoService.save(photoDto);
        return ResponseEntity.created(URI.create(String.format("/api/photo/%s", saved.getId())))
                .body(saved);
    }

    @PutMapping
    ResponseEntity<PhotoDto> update(@RequestBody PhotoDto photoDto){
        return ResponseEntity.ok(this.photoService.save(photoDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.photoService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
