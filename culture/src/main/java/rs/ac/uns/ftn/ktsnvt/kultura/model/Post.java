package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Post {
    @Id
    @Getter
    private UUID id;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    private LocalDateTime timeAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private CulturalOffering culturalOffering;

}
