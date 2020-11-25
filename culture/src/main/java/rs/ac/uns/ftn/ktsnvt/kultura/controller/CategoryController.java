package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/category/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Category>> getAll(@RequestBody Pageable p) {
        // TODO DTO and factory soon
        return new ResponseEntity<>(this.categoryService.readAll(p), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Category> get(@PathVariable String id){
        return new ResponseEntity<>(this.categoryService.readById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<Category> add(@RequestBody Category category){
        return new ResponseEntity<>(this.categoryService.save(category), HttpStatus.CREATED);
    }

    @PutMapping()
    ResponseEntity<Category> update(@RequestBody Category category){
        return new ResponseEntity<>(this.categoryService.save(category), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.categoryService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
