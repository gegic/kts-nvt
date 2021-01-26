import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    private String name;

    @Override
    public String toString() {
        return String.format("INSERT INTO category (name) VALUES ('%s');\n", name.replace("'", "\\'"));
    }
}
