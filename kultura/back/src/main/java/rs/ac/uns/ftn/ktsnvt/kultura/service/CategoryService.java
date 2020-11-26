package rs.ac.uns.ftn.ktsnvt.kultura.service;

import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> readAll(Pageable p) {
        return categoryRepository.findAll(p);
    }

    public Optional<Category> readById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category c) {
        return categoryRepository.save(c);
    }

    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}
