package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/cultural-offering/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CulturalOfferingController {

    private CulturalOfferingService culturalOfferingService;
    private ModelMapper modelMapper;

    @Autowired
    public CulturalOfferingController(CulturalOfferingService culturalOfferingService, ModelMapper modelMapper) {
        this.culturalOfferingService = culturalOfferingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CulturalOfferingDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size,
                                                            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.culturalOfferingService.readAll(p)
                .map(co -> modelMapper.map(co, CulturalOfferingDto.class)));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CulturalOfferingDto> get(@PathVariable String id){
        return ResponseEntity.of(this.culturalOfferingService
                .readById(UUID.fromString(id))
                .map(co -> modelMapper.map(co, CulturalOfferingDto.class)));
    }

    @PostMapping
    ResponseEntity<CulturalOfferingDto> add(@RequestBody CulturalOffering culturalOffering){
        CulturalOffering saved = this.culturalOfferingService.save(culturalOffering);
        return ResponseEntity.created(URI.create("/api/cultural-offering/" + saved.getId()))
                .body(modelMapper.map(saved, CulturalOfferingDto.class));
    }

    @PutMapping
    ResponseEntity<CulturalOfferingDto> update(@RequestBody CulturalOffering culturalOffering){
        return ResponseEntity.ok(modelMapper
                .map(this.culturalOfferingService.save(culturalOffering), CulturalOfferingDto.class));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.culturalOfferingService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
