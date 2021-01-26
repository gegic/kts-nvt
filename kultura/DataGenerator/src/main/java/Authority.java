import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Authority {

    private String authority;

    @Override
    public String toString() {
        return String.format("INSERT INTO authority (authority) VALUES ('%s');\n", authority);
    }
}
