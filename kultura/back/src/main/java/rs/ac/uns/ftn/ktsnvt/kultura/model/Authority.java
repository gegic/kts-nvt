package rs.ac.uns.ftn.ktsnvt.kultura.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="authority")
public class Authority implements GrantedAuthority {

    @Getter
    @Setter
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Getter
    @Setter
    @Column(name="name")
    String name;


    public String getAuthority() {
        return name;
    }
}
