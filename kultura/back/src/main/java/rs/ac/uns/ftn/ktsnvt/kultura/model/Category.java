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
public class Category {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    private Set<Subcategory> subcategories = new HashSet<>();

    @Getter
    @Setter
    @Column(unique = true)
    private String name;

    public void setSubcategories(Set<Subcategory> subcategories) {
        subcategories.forEach(this::addSubcategory);
    }

    public void addSubcategory(Subcategory s) { s.setCategory(this); }
    public void removeSubcategory(Subcategory s) { s.setCategory(null); }

    protected void internalAddSubcategory(Subcategory s) { subcategories.add(s); }
    protected void internalRemoveSubcategory(Subcategory s) { subcategories.remove(s); }

}
