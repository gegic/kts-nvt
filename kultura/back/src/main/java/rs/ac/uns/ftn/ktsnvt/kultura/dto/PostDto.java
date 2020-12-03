package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    private LocalDateTime timeAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @EntityKey(entityType = CulturalOffering.class, fieldName = "culturalOffering")
    private long culturalOfferingId;

}
