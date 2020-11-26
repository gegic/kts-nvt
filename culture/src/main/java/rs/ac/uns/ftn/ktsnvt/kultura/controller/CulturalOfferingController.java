package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/cultural-offering/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingController {

    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOffering>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "3") int size,
                                                         @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.culturalOfferingService.readAll(p));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CulturalOffering> get(@PathVariable String id){
        return ResponseEntity.of(this.culturalOfferingService.readById(UUID.fromString(id)));
    }

    @PostMapping
    ResponseEntity<CulturalOffering> add(@RequestBody CulturalOffering culturalOffering){
        CulturalOffering saved = this.culturalOfferingService.save(culturalOffering);
        return ResponseEntity.created(URI.create("/api/cultural-offering/" + saved.getId())).body(saved);
    }

    @PutMapping
    ResponseEntity<CulturalOffering> update(@RequestBody CulturalOffering culturalOffering){
        return ResponseEntity.ok(this.culturalOfferingService.save(culturalOffering));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.culturalOfferingService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
