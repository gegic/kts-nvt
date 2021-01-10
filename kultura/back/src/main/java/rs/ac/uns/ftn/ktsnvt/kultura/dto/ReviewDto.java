package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityField;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.ReviewPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;

    @NotNull(message = "Review has to have a rating.")
    private Integer rating;

    private String comment;

    @NotNull
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
    private String userEmail;

    @EntityKey(fieldName = "photos", entityType = ReviewPhoto.class)
    private Set<Long> photos;
}
