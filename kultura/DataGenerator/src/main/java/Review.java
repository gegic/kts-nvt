import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {
    String comment;
    int rating;
    long culturalOfferingId;
    long userId;

    @Override
    public String toString() {
        return String.format("INSERT INTO review (comment, rating, time_added, " +
                "cultural_offering_id, user_id) VALUES ('%s', %d, UTC_TIMESTAMP(), %d, %d);\n", comment.replace("'", "\\'"), rating,
                culturalOfferingId, userId);
    }
}
