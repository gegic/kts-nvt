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
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String briefInfo;

    @Getter
    @Setter
    private float latitude;

    @Getter
    @Setter
    private float longitude;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    @EntityKey(entityType = CulturalOfferingPhoto.class, fieldName = "photo")
    private long photoId;

    @Getter
    @Setter
    private float overallRating;

    @Getter
    @Setter
    private int numReviews;

    @Getter
    @Setter
    private LocalDateTime lastChange;

    @Getter
    @Setter
    private String additionalInfo;

    @Getter
    @Setter
    @EntityKey(entityType = Subcategory.class, fieldName = "subcategory")
    private long subcategoryId;

    @Getter
    @Setter
    @EntityField
    private String subcategoryName;
}
