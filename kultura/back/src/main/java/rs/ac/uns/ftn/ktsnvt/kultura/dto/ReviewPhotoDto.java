package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
public class ReviewPhotoDto {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Positive
    private Integer width;

    @Getter
    @Setter
    @Positive
    private Integer height;

    @Getter
    @Setter
    @NotNull
    private LocalDateTime timeAdded;

    @Getter
    @Setter
    @NotNull
    private Long reviewId;
}
