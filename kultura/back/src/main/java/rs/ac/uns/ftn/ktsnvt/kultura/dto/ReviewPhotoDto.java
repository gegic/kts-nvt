package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.EntityKey;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPhotoDto {
    
    private Long id;
    
    private Integer width;
    
    private Integer height;
    
    private LocalDateTime timeAdded;

    @EntityKey(fieldName = "review", entityType = Review.class)
    private Long reviewId;
}
