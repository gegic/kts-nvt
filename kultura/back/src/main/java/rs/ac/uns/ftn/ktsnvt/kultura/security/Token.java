package rs.ac.uns.ftn.ktsnvt.kultura.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;

public class Token extends AbstractAuthenticationToken {

    @Getter
    @Setter
    private String credentials; // TOKEN

    @Getter
    private User principal;

    public Token(User user) {
        super(user.getAuthorities());
        this.principal = user;
    }
}
