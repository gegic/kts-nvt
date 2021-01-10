package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalOfferingPhotoDto {

    private Long id;

    @Positive
    private Integer width;

    @Positive
    private Integer height;

    @NotNull
    private LocalDateTime timeAdded;

    @EntityKey(entityType = CulturalOffering.class, fieldName = "culturalOffering")
    private Long culturalOfferingId;
}
