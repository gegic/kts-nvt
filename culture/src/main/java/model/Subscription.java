package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

public class Subscription {

    @Id
    @Getter
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private CulturalOffering culturalOffering;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private User user;

}
