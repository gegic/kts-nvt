package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.AuthorityConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.AuthorityConstants.roleNonExistent;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class AuthorityRepositoryIntegrationTest {

    @Autowired
    AuthorityRepository authorityRepository;

    @Test
    public void findByAuthorityRoleUser() {
        Authority a = authorityRepository.findByAuthority(AuthorityConstants.roleUser);
        assertEquals(AuthorityConstants.roleUser, a.getAuthority());
    }
}