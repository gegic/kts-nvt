package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/category/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private CategoryService categoryService;
    private ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size,
                                                 @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<CategoryDto> categoryDtos = this.categoryService.readAll(p)
                .map(c -> modelMapper.map(c, CategoryDto.class));
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CategoryDto> get(@PathVariable String id){
        return ResponseEntity.of(this.categoryService.readById(UUID.fromString(id))
                .map(c -> modelMapper.map(c, CategoryDto.class)));
    }

    @PostMapping
    ResponseEntity<CategoryDto> add(@RequestBody Category category){
        Category saved = this.categoryService.save(category);
        return ResponseEntity.created(URI.create("/api/category/" + saved.getId()))
                .body(modelMapper.map(saved, CategoryDto.class));
    }

    @PutMapping
    ResponseEntity<CategoryDto> update(@RequestBody Category category){
        return ResponseEntity.ok(modelMapper.map(this.categoryService.save(category), CategoryDto.class));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.categoryService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
