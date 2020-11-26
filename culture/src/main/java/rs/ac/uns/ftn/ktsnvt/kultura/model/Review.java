package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Review {
    @Id
    @Getter
    private UUID id;

    @Getter
    @Setter
    private int rating;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private LocalDateTime timeAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private CulturalOffering culturalOffering;

}
