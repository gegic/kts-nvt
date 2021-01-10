package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mapper mapper;

    @Test
    public void testReadAll() {
        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        Page<UserDto> userDtoPage = userService.readAll(pageRequest);

        assertEquals(DB_COUNT, userDtoPage.getTotalElements());
    }

    @Test
    public void testReadByAuthority() {
        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        Page<UserDto> userDtoPage = userService.readByAuthority(pageRequest, MODERATOR_AUTHORITY);

        assertEquals(MODERATOR_COUNT, userDtoPage.getTotalElements());
        userDtoPage.forEach(u ->
                assertTrue(u.getAuthorities().stream().anyMatch(a ->
                        a.getAuthority().equals(MODERATOR_AUTHORITY))));
    }

    @Test
    public void testFindById() {
        UserDto user = userService.findById(ADMIN_ID).orElse(null);

        assertEquals(ADMIN_ID, user.getId().longValue());
        assertEquals(ADMIN_EMAIL, user.getEmail());
    }

    @Test
    @Transactional
    //@Rollback
    public void testCreateUser() throws Exception {

        long sizeBefore = userRepository.count();
        UserDto u = createUserDto();

        UserDto createdUser = userService.create(u);

        long sizeAfter = userRepository.count();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(u.getEmail(), createdUser.getEmail());
        assertEquals(u.getFirstName(), createdUser.getFirstName());
        assertEquals(u.getLastName(), createdUser.getLastName());
        assertNull(createdUser.getPassword());

        userService.delete(createdUser.getId());
    }

    @Test
    public void testCreateUserEmailExists() {
        UserDto u = createUserDto();
        u.setEmail(ADMIN_EMAIL);


        ThrowingRunnable throwsException = () -> {
            userService.create(u);
        };

        assertThrows(DataIntegrityViolationException.class, throwsException);
    }

    @Test
    //@Transactional
    //@Rollback
    public void testUpdateUser() throws Exception {
        UserDto oldValues = userService.findById(ADMIN_ID).orElseThrow(() -> new Exception("Test invalid"));

        UserDto u = createUserDto();
        u.setId(ADMIN_ID);
        
        UserDto updated = userService.update(u);

        assertEquals(ADMIN_ID, u.getId().longValue());
        assertEquals(u.getEmail(), updated.getEmail());
        assertEquals(u.getFirstName(), updated.getFirstName());
        assertEquals(u.getLastName(), updated.getLastName());
        assertNull(updated.getPassword());

        userService.update(oldValues);
    }

    @Test
    public void testUpdateUserThrowsResourceNotFoundException() {
        UserDto u = createUserDto();
        u.setId(NON_EXISTENT_ID);

        ThrowingRunnable throwsException = () -> {
            userService.update(u);
        };

        assertThrows(ResourceNotFoundException.class, throwsException);
    }

    @Test
    public void testLoadUserByUsername_Email() {

        User loaded = userService.loadUserByUsername(ADMIN_EMAIL);

        assertEquals(ADMIN_ID, loaded.getId());
        assertEquals(ADMIN_EMAIL, loaded.getEmail());
    }

    @Test
    public void testLoadUserByUsername_UsernameNotFoundException() {

        ThrowingRunnable throwsException = () -> {
            userService.loadUserByUsername(NON_EXISTENT_EMAIL);
        };

        assertThrows(UsernameNotFoundException.class, throwsException);

    }

    @Test
    public void testFindByEmail() {
        Optional<UserDto> found = userService.findByEmail(ADMIN_EMAIL);

        UserDto user = found.orElse(null);

        assertTrue(found.isPresent());
        assertEquals(ADMIN_ID, user.getId().longValue());
        assertEquals(ADMIN_EMAIL, user.getEmail());
    }


    @Test
    public void testFindByEmailNonExistent() {
        Optional<UserDto> found = userService.findByEmail(NON_EXISTENT_EMAIL);

        assertFalse(found.isPresent());
    }

    @Test
    public void testVerify() throws Exception {

        UserDto userDto = userService.verify(UNVERIFIED_ID);

        assertTrue(userDto.isVerified());

        User user = userRepository.findById(userDto.getId()).orElse(null);
        if (user != null) {
            user.setVerified(false);
            userRepository.save(user);
        }
    }

    @Test
    public void testVerifyNonExistent() {
        ThrowingRunnable throwingRunnable = () -> {
            userService.verify(NON_EXISTENT_ID);
        };

        assertThrows(ResourceNotFoundException.class, throwingRunnable);
    }



    private UserDto createUserDto() {
        return  new UserDto(
                null,
                NEW_EMAIL,
                "admin123",
                NEW_FIRST_NAME,
                "last name",
                LocalDateTime.now(),
                true,
                null
        );
    }
}
