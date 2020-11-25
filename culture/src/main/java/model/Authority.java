package model;

import javax.persistence.Id;
import java.util.UUID;

public class Authority {

    @Id
    private UUID id;

    private String name;


    // will override something from ssecurity
    public String getAuthority() {
        return name;
    }
}
