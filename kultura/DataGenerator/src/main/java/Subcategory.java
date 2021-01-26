import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Subcategory {

    private String name;
    private long categoryId;

    @Override
    public String toString() {
        return String.format("INSERT INTO subcategory (name, category_id) VALUES ('%s', %d);\n", name.replace("'", "\\'"), categoryId);
    }}
