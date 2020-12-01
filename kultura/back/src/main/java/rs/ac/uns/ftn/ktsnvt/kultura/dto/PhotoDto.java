package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class PhotoDto {
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
    private UUID culturalOfferingId;
}
