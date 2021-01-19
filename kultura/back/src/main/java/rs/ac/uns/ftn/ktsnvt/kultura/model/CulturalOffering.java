package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CulturalOffering {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    @OneToOne(cascade = CascadeType.ALL)
    private CulturalOfferingMainPhoto photo;

    @Getter
    @Setter
    @ColumnDefault(value = "0")
    private float overallRating;

    @Getter
    @Setter
    @ColumnDefault(value = "0")
    private int numReviews;

    @Getter
    @Setter
    private LocalDateTime lastChange;

    @Getter
    @Setter
    private String additionalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Subcategory subcategory;

    @OneToMany(mappedBy = "culturalOffering", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "culturalOffering", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<CulturalOfferingPhoto> culturalOfferingPhotos = new HashSet<>();

    @OneToMany(mappedBy = "culturalOffering", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Post> posts = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "subscription",
            uniqueConstraints = @UniqueConstraint(columnNames = {"cultural_offering_id", "user_id"}),
            joinColumns = @JoinColumn(name = "cultural_offering_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Getter
    @Setter
    private List<User> subscribedUsers = new ArrayList<>();

    public void setPhoto(CulturalOfferingMainPhoto photo) {
        if (photo == null) {
            if (this.photo != null) {
                this.photo.setCulturalOffering(null);
            }
        }
        else {
            photo.setCulturalOffering(this);
        }
        this.photo = photo;
    }

    public void setSubcategory(Subcategory subcategory) {
        if (this.subcategory != null) { this.subcategory.internalRemoveCulturalOffering(this); }
        this.subcategory = subcategory;
        if (subcategory != null) { subcategory.internalAddCulturalOffering(this); }
    }

    public void setReviews(Set<Review> reviews) {
        reviews.forEach(this::addReview);
    }

    public void addReview(Review s) { s.setCulturalOffering(this); }
    public void removeReview(Review s) { s.setCulturalOffering(null); }

    protected void internalAddReview(Review s) { reviews.add(s); }
    protected void internalRemoveReview(Review s) { reviews.remove(s); }

    public void setPosts(Set<Post> posts) {
        posts.forEach(this::addPost);
    }
    
    public void addPost(Post s) { s.setCulturalOffering(this); }
    public void removePost(Post s) { s.setCulturalOffering(null); }
    
    protected void internalAddPost(Post s) { posts.add(s); }
    protected void internalRemovePost(Post s) { posts.remove(s); }

    public void setCulturalOfferingPhotos(Set<CulturalOfferingPhoto> posts) {
        posts.forEach(this::addCulturalOfferingPhoto);
    }

    public void addCulturalOfferingPhoto(CulturalOfferingPhoto s) { s.setCulturalOffering(this); }
    public void removeCulturalOfferingPhoto(CulturalOfferingPhoto s) { s.setCulturalOffering(null); }

    protected void internalAddCulturalOfferingPhoto(CulturalOfferingPhoto s) { culturalOfferingPhotos.add(s); }
    protected void internalRemoveCulturalOfferingPhoto(CulturalOfferingPhoto s) { culturalOfferingPhotos.remove(s); }

}
