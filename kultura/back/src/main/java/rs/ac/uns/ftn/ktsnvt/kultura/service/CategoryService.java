package rs.ac.uns.ftn.ktsnvt.kultura.service;

import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.CategoryMapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public Page<CategoryDto> readAll(Pageable p) {
        return categoryRepository.findAll(p).map(categoryMapper::fromEntity);
    }

    public Optional<CategoryDto> readById(long id) {
        return categoryRepository.findById(id).map(categoryMapper::fromEntity);
    }

    public CategoryDto save(CategoryDto c) {
        Category toAdd = categoryMapper.fromDto(c);

        return categoryMapper.fromEntity(categoryRepository.save(toAdd));
    }

    public void delete(long id) {
        categoryRepository.deleteById(id);
    }
}
