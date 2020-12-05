package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityField;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class CulturalOfferingDto {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String briefInfo;

    @Getter
    @Setter
    private Float latitude;

    @Getter
    @Setter
    private Float longitude;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    @EntityKey(entityType = CulturalOfferingPhoto.class, fieldName = "photo")
    private Long photoId;

    @Getter
    @Setter
    private Float overallRating;

    @Getter
    @Setter
    private Integer numReviews;

    @Getter
    @Setter
    private LocalDateTime lastChange;

    @Getter
    @Setter
    private String additionalInfo;

    @Getter
    @Setter
    @EntityKey(entityType = Subcategory.class, fieldName = "subcategory")
    private Long subcategoryId;

    @Getter
    @Setter
    @EntityField
    private String subcategoryName;
}
