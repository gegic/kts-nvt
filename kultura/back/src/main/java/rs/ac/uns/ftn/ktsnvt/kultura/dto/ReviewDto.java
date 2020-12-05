package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityField;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;

    private Integer rating;

    @NotBlank(message = "Review comment cannot be blank. You must explain your review.")
    private String comment;

    private LocalDateTime timeAdded;

    @EntityKey(entityType = CulturalOffering.class, fieldName = "culturalOffering")
    private Long culturalOfferingId;

    @EntityKey(entityType = User.class, fieldName = "user")
    private Long userId;

    @EntityField(origin = "user.firstName")
    private String userFirstName;

    @EntityField(origin = "user.lastName")
    private String userLastName;

    @EntityField
    private String userUsername;

    @EntityKey(fieldName = "photos", entityType = CulturalOfferingPhoto.class)
    private Set<Long> photos;
}
