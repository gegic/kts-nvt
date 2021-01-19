package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Post {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Getter
    @Setter
    @Column(length = 1000)
    private String content;
    @Getter
    @Setter
    private LocalDateTime timeAdded = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    private CulturalOffering culturalOffering;

    public void setCulturalOffering(CulturalOffering culturalOffering) {
        if (this.culturalOffering != null) { this.culturalOffering.internalAddPost(this); }
        this.culturalOffering = culturalOffering;
        if (culturalOffering != null) { culturalOffering.internalRemovePost(this); }
    }

}
