package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper mapper;

    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository,
                              Mapper mapper,
                              CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Optional<SubcategoryDto> findById(long id) {
        return subcategoryRepository.findById(id).map(s -> mapper.fromEntity(s, SubcategoryDto.class));
    }

    @Transactional
    public SubcategoryDto create(SubcategoryDto subcategoryDto) {
        Subcategory toAdd = mapper.fromDto(subcategoryDto, Subcategory.class);
        if (subcategoryRepository.existsById(toAdd.getId())) throw new EntityExistsException();
        Subcategory added = subcategoryRepository.save(toAdd);
        Category c = added.getCategory();
        c.getSubcategories().add(added);
        categoryRepository.save(c);

        return mapper.fromEntity(added, SubcategoryDto.class);
    }

    @Transactional
    public SubcategoryDto update(SubcategoryDto subcategoryDto) {
        Subcategory toUpdate = mapper.fromDto(subcategoryDto, Subcategory.class);
        Optional<Subcategory> optionalSubcategory = subcategoryRepository.findById(toUpdate.getId());
        if (!optionalSubcategory.isPresent()) throw new ResourceNotFoundException("Subcategory not found");

        Subcategory existing = optionalSubcategory.get();
        existing.setName(toUpdate.getName());
        return mapper.fromEntity(subcategoryRepository.save(existing), SubcategoryDto.class);
    }


    public Page<SubcategoryDto> findAllByCategoryId(long categoryId, Pageable p) {
        return subcategoryRepository.findAllByCategoryId(categoryId, p)
                .map(s -> mapper.fromEntity(s, SubcategoryDto.class));
    }

    public void delete(long id) {
        Subcategory s = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("A subcategory with the specified id doesn't exist"));

        if (s.getCulturalOfferings() != null && s.getCulturalOfferings().size() > 0) {
            throw new ResourceExistsException("There are cultural offerings associated to this subcategory.");
        }
        s.setCategory(null);
        subcategoryRepository.deleteById(id);
    }
}
