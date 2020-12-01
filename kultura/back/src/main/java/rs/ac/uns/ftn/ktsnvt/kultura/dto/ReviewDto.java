package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class ReviewDto {
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

    @Getter
    @Setter
    private UUID culturalOfferingId;

    @Getter
    @Setter
    private Set<UUID> photos;
}
