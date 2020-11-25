package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PhotoService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/photo/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhotoController {

    @Autowired
    private PhotoService photoService;


    @GetMapping(path = "/{culturalOfferingId}/{pageNumber}/{pageSize}/{sort}/{desc}", produces = "application/json")
    public ResponseEntity<Page<Photo>> get(@PathVariable String culturalOfferingId,
                                     @PathVariable int pageNumber,
                                     @PathVariable int pageSize,
                                     @PathVariable String sort,
                                     @PathVariable boolean desc){
        Pageable p;
        if (sort != null) {
            Sort s;
            if (desc) s = Sort.by(Sort.Direction.DESC, sort);
            else s = Sort.by(Sort.Direction.ASC, sort);
            p = PageRequest.of(--pageNumber, pageSize, s);
        } else p = PageRequest.of(--pageNumber, pageSize);
        return new ResponseEntity<>(this.photoService.readAllByCulturalOfferingId(UUID.fromString(culturalOfferingId),
                p), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<Photo> add(@RequestBody Photo Photo){
        return new ResponseEntity<>(this.photoService.save(Photo), HttpStatus.CREATED);
    }

    @PutMapping()
    ResponseEntity<Photo> update(@RequestBody Photo Photo){
        return new ResponseEntity<>(this.photoService.save(Photo), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.photoService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
