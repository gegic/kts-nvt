package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class CulturalOffering {
    @Id
    @Getter
    private UUID id;

    private String name;

    private String briefInfo;

    private float latitude;

    private float longitude;

    private String address;

    private UUID photoId;

    private float overallRating;

    private int numReviews;

    private LocalDateTime lastChange;

    private String additionalInfo;

    @OneToOne
    private Subcategory subcategory;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HashSet<Review> reviews;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HashSet<Photo> photos;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HashSet<Post> posts;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HashSet<Subscription> subscriptions;

}
