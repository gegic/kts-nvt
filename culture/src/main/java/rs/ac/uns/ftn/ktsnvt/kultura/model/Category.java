package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Getter
    private UUID id;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private HashSet<Subcategory> subcategories;
    @Getter
    @Setter
    private String name;
}
