package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
public class CulturalOfferingPhotoDto {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private Integer width;
    @Getter
    @Setter
    private Integer height;
    @Getter
    @Setter
    private LocalDateTime timeAdded;

    @Getter
    @Setter
    @EntityKey(entityType = CulturalOffering.class, fieldName = "culturalOffering")
    private Long culturalOfferingId;
}
