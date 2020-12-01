package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/photo/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhotoController {

    private PhotoService photoService;
    private ModelMapper modelMapper;

    @Autowired
    public PhotoController(PhotoService photoService, ModelMapper modelMapper) {
        this.photoService = photoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Photo>> get(@PathVariable String culturalOfferingId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.photoService.readAllByCulturalOfferingId(UUID.fromString(culturalOfferingId), p));
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Photo> getById(@PathVariable UUID id) {
        return ResponseEntity.of(this.photoService.readById(id));
    }

    @PostMapping
    ResponseEntity<Photo> add(@RequestBody Photo Photo){
        Photo saved = this.photoService.save(Photo);
        return ResponseEntity.created(URI.create(String.format("/api/photo/%s", saved.getId()))).body(saved);
    }

    @PutMapping
    ResponseEntity<Photo> update(@RequestBody Photo Photo){
        return ResponseEntity.ok(this.photoService.save(Photo));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.photoService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
