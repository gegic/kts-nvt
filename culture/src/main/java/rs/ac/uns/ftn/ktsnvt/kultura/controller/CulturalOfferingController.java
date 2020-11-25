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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/cultural-offering/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingController {

    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOffering>> getAll(@RequestBody Pageable p) {
        // TODO videcemo da li slat cijelu stranu DTOa ili radit neki dto od svega ovoga :D
        return new ResponseEntity<>(this.culturalOfferingService.readAll(p), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CulturalOffering> get(@PathVariable String id){
        return new ResponseEntity<>(this.culturalOfferingService.readById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<CulturalOffering> add(@RequestBody CulturalOffering culturalOffering){
        return new ResponseEntity<>(this.culturalOfferingService.save(culturalOffering), HttpStatus.CREATED);
    }

    @PutMapping()
    ResponseEntity<CulturalOffering> update(@RequestBody CulturalOffering culturalOffering){
        return new ResponseEntity<>(this.culturalOfferingService.save(culturalOffering), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.culturalOfferingService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
