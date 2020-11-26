package rs.ac.uns.ftn.ktsnvt.kultura.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Authority {

    @Id
    private UUID id;

    private String name;


    // will override something from ssecurity
    public String getAuthority() {
        return name;
    }
}
