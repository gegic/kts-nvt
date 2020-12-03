package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/cultural-offerings", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingsController {

    private CulturalOfferingService culturalOfferingService;

    @Autowired
    public CulturalOfferingsController(CulturalOfferingService culturalOfferingService) {
        this.culturalOfferingService = culturalOfferingService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOfferingDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size,
                                                            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.culturalOfferingService.readAll(p));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CulturalOfferingDto> get(@PathVariable String id){
        return ResponseEntity.of(this.culturalOfferingService.readById(Long.parseLong(id)));
    }

    @PostMapping
    ResponseEntity<CulturalOfferingDto> add(@RequestBody CulturalOfferingDto culturalOfferingDto){
        CulturalOfferingDto saved = this.culturalOfferingService.create(culturalOfferingDto);
        return ResponseEntity.created(URI.create("/api/cultural-offering/" + saved.getId())).body(saved);
    }

    @PutMapping
    ResponseEntity<CulturalOfferingDto> update(@RequestBody CulturalOfferingDto culturalOfferingDto){
        return ResponseEntity.ok(this.culturalOfferingService.update(culturalOfferingDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.culturalOfferingService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
