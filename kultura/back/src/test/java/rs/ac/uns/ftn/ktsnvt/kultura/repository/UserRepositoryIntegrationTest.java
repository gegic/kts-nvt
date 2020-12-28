package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import static org.junit.Assert.*;

import java.util.Optional;

import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail() {
        Optional<User> u = userRepository.findByEmail(ADMIN_EMAIL);

        assertTrue(u.isPresent());
        assertEquals(ADMIN_EMAIL, u.get().getEmail());
    }

    @Test
    public void findByAuthority() {
        Pageable p = PageRequest.of(0, 3);
        Page<User> userPage = userRepository.findByAuthority(MODERATOR_AUTHORITY, p);

        assertEquals(MODERATOR_COUNT, userPage.getTotalElements());
        userPage.forEach(u -> u.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(MODERATOR_AUTHORITY)));
    }
}
