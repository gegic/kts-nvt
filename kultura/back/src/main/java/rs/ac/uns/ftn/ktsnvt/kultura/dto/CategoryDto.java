package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class CategoryDto {
    @Getter
    private UUID id;

    @Getter
    @Setter
    private String name;
}
