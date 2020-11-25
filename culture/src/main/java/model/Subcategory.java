package model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

public class Subcategory {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private String name;

    @OneToMany(mappedBy = "subcategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CulturalOffering> culturalOfferings;


}
