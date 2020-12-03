package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.SubcategoryMapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private SubcategoryMapper subcategoryMapper;

    public Optional<Subcategory> findById(long id) {
        return subcategoryRepository.findById(id);
    }

    public Subcategory save(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    public Page<SubcategoryDto> findAllByCategoryId(long categoryId, Pageable p) {
        return subcategoryRepository.findAllByCategoryId(categoryId, p).map(subcategoryMapper::fromEntity);
    }
}
