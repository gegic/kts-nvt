package rs.ac.uns.ftn.ktsnvt.kultura.service;

import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;

import java.util.Optional;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, Mapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public Page<CategoryDto> readAll(Pageable p) {
        return categoryRepository.findAll(p).map(c -> mapper.fromEntity(c, CategoryDto.class));
    }

    public Optional<CategoryDto> readById(long id) {
        return categoryRepository.findById(id).map(c -> mapper.fromEntity(c, CategoryDto.class));
    }

    public CategoryDto save(CategoryDto c) {
        Category toAdd = mapper.fromDto(c, Category.class);

        return mapper.fromEntity(categoryRepository.save(toAdd), CategoryDto.class);
    }

    public void delete(long id) {
        categoryRepository.deleteById(id);
    }
}
