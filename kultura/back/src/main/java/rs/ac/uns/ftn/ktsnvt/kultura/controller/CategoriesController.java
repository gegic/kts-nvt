package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.SubcategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriesController {

    private final CategoryService categoryService;

    @Autowired
    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id,asc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<CategoryDto> categoryDtos = this.categoryService.readAll(p);
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CategoryDto> get(@PathVariable long id){
        return ResponseEntity.of(this.categoryService.readById(id));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<CategoryDto> add(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto saved = this.categoryService.create(categoryDto);
        return ResponseEntity.created(URI.create("/api/category/" + saved.getId())).body(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(this.categoryService.update(categoryDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.categoryService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }



}
