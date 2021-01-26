import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Photo {
    private long culturalOfferingId;

    @Override
    public String toString() {
        return String.format("INSERT INTO kts.cultural_offering_photo (height, time_added, width, cultural_offering_id) " +
                "VALUES (1000, UTC_TIMESTAMP(), 667, %d);\n", culturalOfferingId);
    }
}
