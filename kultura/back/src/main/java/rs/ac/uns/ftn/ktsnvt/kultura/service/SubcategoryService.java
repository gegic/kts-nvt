package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import java.util.Optional;

@Service
public class SubcategoryService {

    private SubcategoryRepository subcategoryRepository;
    private Mapper mapper;

    @Autowired
    public SubcategoryService(SubcategoryRepository categoryRepository, Mapper mapper) {
        this.subcategoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public Optional<Subcategory> findById(long id) {
        return subcategoryRepository.findById(id);
    }

    public Subcategory save(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    public Page<SubcategoryDto> findAllByCategoryId(long categoryId, Pageable p) {
        return subcategoryRepository.findAllByCategoryId(categoryId, p)
                .map(s -> mapper.fromEntity(s, SubcategoryDto.class));
    }
}
