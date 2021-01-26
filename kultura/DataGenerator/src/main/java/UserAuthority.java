import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthority {
    long userId;
    long authorityId;

    @Override
    public String toString() {
        return String.format("INSERT INTO user_authority (user_id, authority_id) VALUES (%d, %d);\n", userId, authorityId);
    }
}
