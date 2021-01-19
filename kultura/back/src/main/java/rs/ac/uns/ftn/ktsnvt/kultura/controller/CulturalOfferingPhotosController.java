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

@RestController
@RequestMapping(path = "/api/photos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingPhotosController {

    private final CulturalOfferingPhotoService culturalOfferingPhotoService;

    @Autowired
    public CulturalOfferingPhotosController(CulturalOfferingPhotoService culturalOfferingPhotoService) {
        this.culturalOfferingPhotoService = culturalOfferingPhotoService;
    }

    @GetMapping(path = "/cultural-offering/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOfferingPhotoDto>> get(@PathVariable long culturalOfferingId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "timeAdded,desc") String[] sort){
        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<CulturalOfferingPhotoDto> photoDtos = this.culturalOfferingPhotoService
                .readAllByCulturalOfferingId(culturalOfferingId, p);
        return ResponseEntity.ok(photoDtos);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping(path="/cultural-offering/{culturalOfferingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CulturalOfferingPhotoDto> add(@RequestParam("photo") MultipartFile photoFile,
                                                 @PathVariable long culturalOfferingId){
        CulturalOfferingPhotoDto saved = this.culturalOfferingPhotoService.create(photoFile, culturalOfferingId);
        return ResponseEntity.created(URI.create(saved.getId().toString())).body(saved);
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id){
        this.culturalOfferingPhotoService.delete(id);
        return ResponseEntity.ok().build();
    }

}
