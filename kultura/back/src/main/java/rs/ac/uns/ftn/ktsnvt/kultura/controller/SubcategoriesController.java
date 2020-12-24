package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.SubcategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;

@PreAuthorize("hasRole('MODERATOR') || hasRole('ADMIN')")
@RestController
@RequestMapping(path = "/api/subcategories", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubcategoriesController {
    private final SubcategoryService subcategoryService;

    @Autowired
    public SubcategoriesController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @PreAuthorize("hasRole('MODERATOR') || hasRole('ADMIN')")
    @GetMapping("/category/{categoryId}")
    ResponseEntity<Page<SubcategoryDto>> getSubcategoriesByCategoryId(@PathVariable long categoryId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "id,asc") String[] sort) {
        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.subcategoryService.findAllByCategoryId(categoryId, p));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<SubcategoryDto> create(@Valid @RequestBody SubcategoryDto subcategoryDto) {
        try {
            SubcategoryDto saved = subcategoryService.create(subcategoryDto);
            return ResponseEntity.created(URI.create(String.format("/%d", saved.getId()))).body(saved);
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    ResponseEntity<SubcategoryDto> update(@Valid @RequestBody SubcategoryDto subcategoryDto) {
        try {
            SubcategoryDto updated = subcategoryService.update(subcategoryDto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id) {
        subcategoryService.delete(id);
        return ResponseEntity.ok().build();
    }


}
