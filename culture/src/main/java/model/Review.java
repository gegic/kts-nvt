package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Review {
    @Id
    private UUID id;

    private int rating;

    private String comment;

    private LocalDateTime timeAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    private CulturalOffering culturalOffering;

}
