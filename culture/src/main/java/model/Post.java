package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Post {
    @Id
    private UUID id;
    private String content;
    private LocalDateTime timeAdded;

    @ManyToOne(fetch = FetchType.LAZY)
    private CulturalOffering culturalOffering;

}
