package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class PostDto {

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
    private UUID culturalOfferingId;

}
