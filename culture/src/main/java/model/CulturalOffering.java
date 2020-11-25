package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String briefInfo;

    @Getter
    @Setter
    private float latitude;

    @Getter
    @Setter
    private float longitude;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private UUID photoId;

    @Getter
    @Setter
    private float overallRating;

    @Getter
    @Setter
    private int numReviews;

    @Getter
    @Setter
    private LocalDateTime lastChange;

    @Getter
    @Setter
    private String additionalInfo;

    @OneToOne
    @Getter
    @Setter
    private Subcategory subcategory;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private HashSet<Review> reviews;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private HashSet<Photo> photos;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private HashSet<Post> posts;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private HashSet<Subscription> subscriptions;

}
