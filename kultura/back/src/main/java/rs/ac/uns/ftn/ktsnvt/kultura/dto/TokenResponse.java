package rs.ac.uns.ftn.ktsnvt.kultura.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private UserDto user;
}
