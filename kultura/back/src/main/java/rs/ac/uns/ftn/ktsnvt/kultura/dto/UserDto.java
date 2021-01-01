package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Ignore;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.IgnoreType;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @Email(message = "You entered an invalid email address.")
    private String email;

    @Ignore(ignoreType = IgnoreType.ENTITY_TO_DTO)
    @Size(min=8, max=50, message = "A password should be between 8 and 50 characters long.")
    private String password;

    @NotBlank(message = "First name of user cannot be blank.")
    private String firstName;

    @NotBlank(message = "Last name of user cannot be blank.")
    private String lastName;

    private LocalDateTime lastPasswordChange;

    @Ignore(ignoreType = IgnoreType.DTO_TO_ENTITY)
    private boolean verified;

    @Ignore(ignoreType = IgnoreType.DTO_TO_ENTITY)
    private Set<Authority> authorities;
}
