package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/photo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhotoController {

    private PhotoService photoService;
    private ModelMapper modelMapper;

    @Autowired
    public PhotoController(PhotoService photoService, ModelMapper modelMapper) {
        this.photoService = photoService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PhotoDto>> get(@PathVariable String culturalOfferingId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<PhotoDto> photoDtos = this.photoService
                .readAllByCulturalOfferingId(Long.parseLong(culturalOfferingId), p)
                .map(photo -> modelMapper.map(photo, PhotoDto.class));
        return ResponseEntity.ok(photoDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.photoService.readById(id).map(photo -> modelMapper.map(photo, PhotoDto.class)));
    }

    @PostMapping
    ResponseEntity<PhotoDto> add(@RequestBody Photo Photo){
        Photo saved = this.photoService.save(Photo);
        return ResponseEntity.created(URI.create(String.format("/api/photo/%s", saved.getId())))
                .body(modelMapper.map(saved, PhotoDto.class));
    }

    @PutMapping
    ResponseEntity<PhotoDto> update(@RequestBody Photo Photo){
        return ResponseEntity.ok(modelMapper.map(this.photoService.save(Photo), PhotoDto.class));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.photoService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
