package rs.ac.uns.ftn.ktsnvt.kultura.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;

@Component
public class SubcategoryMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    public Subcategory fromDto(SubcategoryDto s) {
        Category c = categoryRepository.findById(s.getCategoryId()).orElseThrow(EntityNotFoundException::new);
        return new Subcategory(s.getId(), c, s.getName(), null);
    }

    public SubcategoryDto fromEntity(Subcategory s) {
        return new SubcategoryDto(s.getId(), s.getName(), s.getCategory().getId());
    }
}
