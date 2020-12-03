package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    private String name;
}
