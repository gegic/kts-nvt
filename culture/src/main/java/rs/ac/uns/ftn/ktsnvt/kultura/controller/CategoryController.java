package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/category/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Category>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size,
                                                 @RequestParam(defaultValue = "id,desc") String[] sort) {

        // TODO DTO and factory soon

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.categoryService.readAll(p));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Category> get(@PathVariable String id){
        return ResponseEntity.of(this.categoryService.readById(UUID.fromString(id)));
    }

    @PostMapping
    ResponseEntity<Category> add(@RequestBody Category category){
        Category saved = this.categoryService.save(category);
        return ResponseEntity.created(URI.create("/api/category/" + saved.getId())).body(saved);
    }

    @PutMapping
    ResponseEntity<Category> update(@RequestBody Category category){
        return ResponseEntity.ok(this.categoryService.save(category));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.categoryService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
