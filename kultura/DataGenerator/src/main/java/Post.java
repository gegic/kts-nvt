import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    String content;
    long culturalOfferingId;

    @Override
    public String toString() {
        return String.format("INSERT INTO post (content, time_added, cultural_offering_id) " +
                "VALUES ('%s', UTC_TIMESTAMP(), %d);\n", content.replace("'", "\\'"), culturalOfferingId);
    }
}
