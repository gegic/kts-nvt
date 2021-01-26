import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CulturalOffering {
    long id;
    String name;
    String address;
    String briefInfo;
    double longitude;
    double latitude;
    long subcategoryId;
    long numReviews;
    double overallRating;

    public void addRating(Review s) {
        double overallRating = this.getOverallRating();
        long numReviews = this.getNumReviews();

        long newNumReviews = numReviews + 1;

        double newOverallRating = (overallRating * numReviews + s.getRating()) / newNumReviews;
        this.setOverallRating(newOverallRating);
        this.setNumReviews(newNumReviews);
    }

    @Override
    public String toString() {
        return String.format("INSERT INTO cultural_offering_main_photo (height, time_added, width, token) VALUES (288, UTC_TIMESTAMP(), 512, '');\n" +
                "\n" +
                "INSERT INTO cultural_offering (id, address, brief_info, latitude, longitude, name, subcategory_id, photo_id, num_reviews, overall_rating) VALUES (%d, '%s', '%s', %f, %f, '%s', %d, %d, %d, %f);\n" +
                "\n" +
                "UPDATE cultural_offering_main_photo SET cultural_offering_id = %d WHERE id = %d;\n", id, address.replace("'", "\\'"), briefInfo.replace("'", "\\'"), latitude, longitude, name.replace("'", "\\'"), subcategoryId, id, numReviews, overallRating, id, id);
    }
}
