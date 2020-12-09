package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Ignore;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.IgnoreType;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;
    @Size(min=8, max=50, message = "A username should be between 8 and 50 characters long.")
    private String username;
    @Email(message = "You entered an invalid email address.")
    private String email;

    @Ignore(ignoreType = IgnoreType.ENTITY_TO_DTO)
    @Size(min=8, max=50, message = "A password should be between 8 and 50 characters long.")
    private String password;

    private String firstName;

    private String lastName;

    private LocalDateTime lastPasswordChange;

    @Ignore(ignoreType = IgnoreType.DTO_TO_ENTITY)
    private boolean verified;

    @Ignore(ignoreType = IgnoreType.DTO_TO_ENTITY)
    private Set<String> authorities;
}
