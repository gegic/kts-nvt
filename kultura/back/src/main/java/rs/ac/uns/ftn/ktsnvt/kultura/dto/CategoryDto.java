package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Computed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class CategoryDto {

    @Getter
    @Setter
    private Long id;

//    @Size(min = 2, message
//            = "Category name must have at least 2 characters")
    @NotBlank( message = "Category name cannot be blank.")
    @Getter
    @Setter
    private String name;

    @Computed(element = "subcategories", functionName = "size")
    private Integer numSubcategories;
}
