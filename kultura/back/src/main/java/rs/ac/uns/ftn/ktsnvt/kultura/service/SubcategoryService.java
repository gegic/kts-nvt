package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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

    @Transactional
    public Optional<SubcategoryDto> findById(long id) {
        return subcategoryRepository.findById(id).map(s -> mapper.fromEntity(s, SubcategoryDto.class));
    }

    @Transactional
    public SubcategoryDto create(SubcategoryDto subcategoryDto) {
        Subcategory toAdd = mapper.fromDto(subcategoryDto, Subcategory.class);
        if (subcategoryRepository.existsById(toAdd.getId())) throw new EntityExistsException();

        return mapper.fromEntity(subcategoryRepository.save(toAdd), SubcategoryDto.class);
    }

    @Transactional
    public SubcategoryDto update(SubcategoryDto subcategoryDto) {
        Subcategory toUpdate = mapper.fromDto(subcategoryDto, Subcategory.class);
        if (!subcategoryRepository.existsById(toUpdate.getId())) throw new EntityNotFoundException();

        return mapper.fromEntity(subcategoryRepository.save(toUpdate), SubcategoryDto.class);
    }


    public Page<SubcategoryDto> findAllByCategoryId(long categoryId, Pageable p) {
        return subcategoryRepository.findAllByCategoryId(categoryId, p)
                .map(s -> mapper.fromEntity(s, SubcategoryDto.class));
    }

    public void delete(long id) {
        subcategoryRepository.deleteById(id);
    }
}
