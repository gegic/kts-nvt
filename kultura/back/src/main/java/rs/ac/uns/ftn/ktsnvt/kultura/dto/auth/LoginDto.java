package rs.ac.uns.ftn.ktsnvt.kultura.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class LoginDto {

    @Getter
    @Email
    private String email;
    @Getter
    @Size(min=8, max=50, message = "A username should be between 8 and 50 characters long.")
    @Pattern(regexp="(?=[a-zA-Z0-9]{8})(?=a-z+)(?=A-Z+)(?=0-9+)",message="length must be 8 or more")
    private String password;
}
