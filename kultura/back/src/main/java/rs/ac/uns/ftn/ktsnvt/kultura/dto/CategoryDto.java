package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;
}
