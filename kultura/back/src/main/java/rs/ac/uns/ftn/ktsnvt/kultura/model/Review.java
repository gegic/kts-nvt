package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"cultural_offering_id", "user_id"})
)
@Entity
public class Review {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private LocalDateTime timeAdded = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private CulturalOffering culturalOffering;

    @ManyToOne(fetch = FetchType.EAGER)
    @Getter
    @Setter
    private User user;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<ReviewPhoto> photos;

}