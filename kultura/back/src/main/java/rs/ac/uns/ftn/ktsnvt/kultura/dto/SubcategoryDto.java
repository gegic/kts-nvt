package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Computed;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityField;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryDto {

    private Long id;

    @NotBlank(message = "You must enter the name of the subcategory.")
    private String name;

    @EntityKey(entityType = Category.class, fieldName = "category")
    private Long categoryId;

    @EntityField
    private String categoryName;

    @Computed(element = "culturalOfferings", functionName = "size")
    private Integer numOfferings;

}
