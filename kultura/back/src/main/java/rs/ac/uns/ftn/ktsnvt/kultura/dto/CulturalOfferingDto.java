package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityField;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingProfilePhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CulturalOfferingDto {

    private Long id;

    @NotBlank(message = "The name of the cultural offering cannot be blank.")
    private String name;

    private String briefInfo;

    //dodati posle min/max za ovo kad skontamo na osnovu mape koje su te vrednosti koje omogucujemo
    private Float latitude;

    private Float longitude;

    private String address;

    @EntityKey(entityType = CulturalOfferingProfilePhoto.class, fieldName = "photo")
    private Long photoId;

    private Float overallRating;

    private Integer numReviews;

    private LocalDateTime lastChange;

    private String additionalInfo;

    @EntityKey(entityType = Subcategory.class, fieldName = "subcategory")
    private Long subcategoryId;

    @EntityField
    private String subcategoryName;
}
