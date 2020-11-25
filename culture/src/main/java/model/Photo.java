package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Photo {
    @Id
    private UUID id;
    private int width;
    private int height;
    private LocalDateTime timeAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    private CulturalOffering culturalOffering;

}
