package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class ReviewPhotoDto {
    @Getter
    private UUID id;
    @Getter
    @Setter
    private int width;
    @Getter
    @Setter
    private int height;
    @Getter
    @Setter
    private LocalDateTime timeAdded;
    @Getter
    @Setter
    private UUID reviewId;
}
