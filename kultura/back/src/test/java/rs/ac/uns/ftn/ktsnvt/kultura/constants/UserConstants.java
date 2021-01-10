package rs.ac.uns.ftn.ktsnvt.kultura.constants;

import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;

import java.time.LocalDateTime;

public class UserConstants {
    public static final int PAGE_SIZE = 3;

    public static final int DB_COUNT = 5;

    public static final String MODERATOR_AUTHORITY = "ROLE_MODERATOR";

    public static final int MODERATOR_COUNT = 1;

    public static final String ADMIN_EMAIL = "admin@mail.com";
    public static final String ADMIN_PASSWORD = "admin123";
    public static final long ADMIN_ID = 1;
    public static final String ADMIN_FULL_NAME = "Admin Prezime";

    public static final String MODERATOR_EMAIL = "moderator@mail.com";
    public static final String MODERATOR_PASSWORD = "admin123";

    public static final long USER_ID = 3;
    public static final String USER_EMAIL = "user@mail.com";

    public static final String NEW_EMAIL = "new.user@mail.com";
    public static final String NEW_FIRST_NAME = "name";

    public static final long NON_EXISTENT_ID = 22L;
    public static final String NON_EXISTENT_EMAIL = "nomail@mail.nomail";

    public static final long UNVERIFIED_ID = 4L;
    public static final long UNVERIFIED_ID2 = 4L;

    public static final long EXISTING_USER_ID = 1;
    public static final String EXISTING_USER_EMAIL = "existing.user@mail.com";
    public static final String EXISTING_USER_FIRST_NAME = "Korisnik";
    public static final String EXISTING_USER_LAST_NAME = "Prezime";
    public static final String EXISTING_USER_PASSWORD = "hashed password";
    public static final String USER_AUTHORITY = "ROLE_USER";
}
