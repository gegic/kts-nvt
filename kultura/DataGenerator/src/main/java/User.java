import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String email;
    boolean verified;
    String firstName;
    String lastName;

    @Override
    public String toString() {
        return String.format("INSERT INTO user (email, first_name, last_name, last_password_change, password, verified) " +
                "VALUES ('%s', '%s', '%s', UTC_TIMESTAMP(), " +
                "'$2a$10$yu3dC4LLPclLT9XAmvVtiuygJdD4kXMUnfBtu5k2.SxlrabVbR/vy', %b);\n", email.replace("'", "\\'"), firstName.replace("'", "\\'"), lastName.replace("'", "\\'"), verified);
    }
}
