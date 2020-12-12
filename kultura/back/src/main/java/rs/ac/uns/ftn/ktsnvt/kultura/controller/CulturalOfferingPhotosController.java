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
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingPhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;

@PreAuthorize("hasRole('MODERATOR') || hasRole('USER')")
@RestController
@RequestMapping(path = "/api/photos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingPhotosController {

    private final CulturalOfferingPhotoService culturalOfferingPhotoService;

    @Autowired
    public CulturalOfferingPhotosController(CulturalOfferingPhotoService culturalOfferingPhotoService) {
        this.culturalOfferingPhotoService = culturalOfferingPhotoService;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOfferingPhotoDto>> get(@PathVariable long culturalOfferingId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "3") int size,
                                                              @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<CulturalOfferingPhotoDto> photoDtos = this.culturalOfferingPhotoService
                .readAllByCulturalOfferingId(culturalOfferingId, p);
        return ResponseEntity.ok(photoDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CulturalOfferingPhotoDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.culturalOfferingPhotoService.readById(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CulturalOfferingPhotoDto> add(@RequestParam("photo") MultipartFile photoFile){
        CulturalOfferingPhotoDto saved = this.culturalOfferingPhotoService.create(photoFile);
        return ResponseEntity.created(URI.create(String.format("/api/photo/%s", saved.getId())))
                .body(saved);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CulturalOfferingPhotoDto> update(@RequestParam("photo") MultipartFile photoFile,
                                                    @PathVariable long id){
        return ResponseEntity.ok(this.culturalOfferingPhotoService.update(photoFile, id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.culturalOfferingPhotoService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
