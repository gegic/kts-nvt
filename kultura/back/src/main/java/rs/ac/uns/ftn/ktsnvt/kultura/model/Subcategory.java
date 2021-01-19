package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;



@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Subcategory {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Category category;

    @Getter
    @Setter
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.LAZY)
    @Getter
    private Set<CulturalOffering> culturalOfferings = new HashSet<>();

    public void setCategory(Category category) {
        if (this.category != null) { this.category.internalRemoveSubcategory(this); }
        this.category = category;
        if (category != null) { category.internalAddSubcategory(this); }
    }

    public void setCulturalOfferings(Set<CulturalOffering> culturalOfferings) {
        culturalOfferings.forEach(this::addCulturalOffering);
    }

    public void addCulturalOffering(CulturalOffering s) { s.setSubcategory(this); }
    public void removeCulturalOffering(CulturalOffering s) { s.setSubcategory(null); }

    protected void internalAddCulturalOffering(CulturalOffering s) { culturalOfferings.add(s); }
    protected void internalRemoveCulturalOffering(CulturalOffering s) { culturalOfferings.remove(s); }

}
