package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.SubcategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private CategoryService categoryService;
    private SubcategoryService subcategoryService;

    @Autowired
    public CategoryController(CategoryService categoryService, SubcategoryService subcategoryService) {
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CategoryDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size,
                                                    @RequestParam(defaultValue = "id,desc") String[] sort) {

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<CategoryDto> categoryDtos = this.categoryService.readAll(p);
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<CategoryDto> get(@PathVariable long id){
        return ResponseEntity.of(this.categoryService.readById(id));
    }

    @PostMapping
    ResponseEntity<CategoryDto> add(@RequestBody CategoryDto categoryDto){
        CategoryDto saved = this.categoryService.save(categoryDto);
        return ResponseEntity.created(URI.create("/api/category/" + saved.getId())).body(saved);
    }

    @PutMapping
    ResponseEntity<CategoryDto> update(@RequestBody CategoryDto categoryDto){
        return ResponseEntity.ok(this.categoryService.save(categoryDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.categoryService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subcategory/{categoryId}")
    ResponseEntity<Page<SubcategoryDto>> getSubcategoriesByCategoryId(@PathVariable long categoryId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "3") int size,
                                                                @RequestParam(defaultValue = "id,desc") String[] sort) {
        Pageable p = PageableExtractor.extract(page, size, sort);

        return ResponseEntity.ok(this.subcategoryService.findAllByCategoryId(categoryId, p));
    }

}
