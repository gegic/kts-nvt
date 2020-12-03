package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Ignore;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.IgnoreType;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    @Ignore(ignoreType = IgnoreType.ENTITY_TO_DTO)
    private String password;

    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private LocalDateTime lastPasswordChange;
    @Getter
    @Setter
    private boolean verified;

    @Getter
    @Setter
    private Set<String> authorities;
}
