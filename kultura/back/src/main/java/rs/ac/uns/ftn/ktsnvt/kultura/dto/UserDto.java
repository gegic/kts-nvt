package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class UserDto {

    @Getter
    private UUID id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String email;

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
