package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.validators.NullOrNotBlank;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Ignore;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.IgnoreType;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    public interface PostGroup{

    }
    public interface PutGroup{

    }

    private Long id;
    @Email(message = "You entered an invalid email address.", groups = {PostGroup.class, PutGroup.class})
    @NotNull(groups = PostGroup.class)
    private String email;

    @Ignore(ignoreType = IgnoreType.ENTITY_TO_DTO)
    @Size(min=8, max=50, message = "A password should be between 8 and 50 characters long.", groups = {PostGroup.class, PutGroup.class})
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", groups = {PostGroup.class, PutGroup.class})
    @NotNull(groups = PostGroup.class)
    private String password;

    @NotBlank(groups = PostGroup.class)
    @NullOrNotBlank(groups = PutGroup.class)
    private String firstName;


    @NotBlank(groups = PostGroup.class)
    @NullOrNotBlank(groups = PutGroup.class)
    private String lastName;

    private LocalDateTime lastPasswordChange;

    @Ignore(ignoreType = IgnoreType.DTO_TO_ENTITY)
    private boolean verified;

    @Ignore(ignoreType = IgnoreType.DTO_TO_ENTITY)
    private Set<Authority> authorities;
}
