package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class CulturalOfferingDto {
    @Getter
    private UUID id;

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
    private UUID photoId;

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
    private UUID subcategoryId;

    @Getter
    @Setter
    private String subcategoryName;
}
