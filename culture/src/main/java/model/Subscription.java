package model;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

public class Subscription {
    @ManyToOne(fetch = FetchType.LAZY)
    private CulturalOffering culturalOffering;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
