package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

public class Subcategory {

    @Id
    @Getter
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Category category;

    @Getter
    @Setter
    private String name;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private List<CulturalOffering> culturalOfferings;


}
