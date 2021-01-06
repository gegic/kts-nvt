package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.AuthorityRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UserServiceUnitTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private Mapper mapper;
    @MockBean
    private AuthorityRepository authorityRepository;
    @MockBean
    private SMTPServer smtpServer;

    @Autowired
    private UserService userService;


    private Authority getUserAuthority() {
        Authority a = new Authority();
        a.setId(1);
        a.setAuthority(USER_AUTHORITY);
        return a;
    }

    private User getFirstUser() {
        User newUser = new User();
        newUser.setId(EXISTING_USER_ID);
        newUser.setEmail(EXISTING_USER_EMAIL);
        newUser.setFirstName(EXISTING_USER_FIRST_NAME);
        newUser.setLastName(EXISTING_USER_LAST_NAME);
        newUser.setLastPasswordChange(LocalDateTime.of(2020,7,23,0,0));
        newUser.setVerified(true);
        newUser.setPassword(EXISTING_USER_PASSWORD);
        Authority a = getUserAuthority();
        Set<Authority> authorities = new HashSet<>();
        authorities.add(a);
        newUser.setAuthorities(authorities);
        return newUser;
    }

    private UserDto getFirstUserDto() {
        Authority a = getUserAuthority();
        Set<Authority> authorities = new HashSet<>();
        authorities.add(a);
        return new UserDto(
                EXISTING_USER_ID,
                EXISTING_USER_EMAIL,
                EXISTING_USER_PASSWORD,
                EXISTING_USER_FIRST_NAME,
                EXISTING_USER_LAST_NAME,
                LocalDateTime.of(2020,7,23,0,0),
                true,
                authorities
        );
    }
    private UserDto getNewUserDto() {
        Authority a = getUserAuthority();
        Set<Authority> authorities = new HashSet<>();
        authorities.add(a);
        return new UserDto(
                null,
                NEW_USER_EMAIL,
                NEW_USER_PASSWORD,
                NEW_USER_FIRST_NAME,
                NEW_USER_LAST_NAME,
                LocalDateTime.of(2020,7,23,0,0),
                false,
                authorities
        );
    }


    private UserDto getUpdateUserDto(){
        UserDto u = getNewUserDto();
        u.setId(1L);
        return u;
    }

    @Test
    public void testReadByAuthority() {
        User existingUser = getFirstUser();
        UserDto existingUserDto = getFirstUserDto();
        Mockito.when(userRepository.findByAuthority(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenAnswer(
                        i -> {
                            List<User> content = new ArrayList<>();
                            String auth = i.getArgument(0);
                            Pageable p = i.getArgument(1);
                            if (existingUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(auth))) {
                                content.add(existingUser);
                            }
                            return (Page<User>) new HelperPage<User>(content, p);
                        }
                );

        Mockito.when(mapper.fromEntity(Mockito.any(User.class), Mockito.eq(UserDto.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            return new UserDto(
                    u.getId(),
                    u.getEmail(),
                    null,
                    u.getFirstName(),
                    u.getLastName(),
                    u.getLastPasswordChange(),
                    u.isVerified(),
                    (Set<Authority>) u.getAuthorities()
            );
        });

        Pageable p = PageRequest.of(0, 3);
        Page<UserDto> userDtoPage = userService.readByAuthority(p, USER_AUTHORITY);

        UserDto userDto = userDtoPage.getContent().get(0);
        assertEquals(1, userDtoPage.getTotalElements());
        assertTrue(userDto.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(USER_AUTHORITY)));
    }

    @Test
    public void testLoadUserByUsername() {
        User existingUser = getFirstUser();
        Mockito.when(userRepository.findByEmail(EXISTING_USER_EMAIL))
                .thenReturn(Optional.of(existingUser));

        User user = userService.loadUserByUsername(EXISTING_USER_EMAIL);

        assertEquals(EXISTING_USER_EMAIL, user.getEmail());
        assertEquals(EXISTING_USER_ID, user.getId());
    }

    @Test
    public void testSendMail() throws Exception {
        User existingUser = getFirstUser();
        final int[] timesCalled = {0};
        final String[] sentTo = new String[1];
        final String[] subject = new String[1];
        final String[] body = new String[1];
        Mockito.doNothing()
                .when(smtpServer).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        userService.sendMail(existingUser);

        Mockito.verify(smtpServer)
                .sendEmail(Mockito.eq(existingUser.getEmail()),
                        Mockito.anyString(),
                        Mockito.contains(String.valueOf(existingUser.getId())));
    }

    @Test
    public void testVerify() throws Exception {
        User existingUser = getFirstUser();
        existingUser.setVerified(false);

        UserDto existingUserDto = getFirstUserDto();
        existingUserDto.setVerified(false);

        Mockito.when(userRepository.findById(EXISTING_USER_ID)).thenReturn(Optional.of(existingUser));

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(User.class), Mockito.eq(UserDto.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            return new UserDto(
                    u.getId(),
                    u.getEmail(),
                    null,
                    u.getFirstName(),
                    u.getLastName(),
                    u.getLastPasswordChange(),
                    u.isVerified(),
                    (Set<Authority>) u.getAuthorities()
            );
        });

        UserDto modified = userService.verify(EXISTING_USER_ID);

        assertTrue(modified.isVerified());
        assertEquals(EXISTING_USER_ID, modified.getId().longValue());
    }

    @Test
    public void testCreateUser(){

        UserDto newUser = getNewUserDto();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(User.class), Mockito.eq(UserDto.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            return new UserDto(
                    u.getId(),
                    u.getEmail(),
                    null,
                    u.getFirstName(),
                    u.getLastName(),
                    u.getLastPasswordChange(),
                    u.isVerified(),
                    (Set<Authority>) u.getAuthorities()
            );
        });

        UserDto createdUser = userService.create(newUser);

        assertEquals(createdUser.getEmail(), newUser.getEmail());
        assertEquals(createdUser.getFirstName(), newUser.getFirstName());
        assertEquals(createdUser.getLastName(), newUser.getLastName());
    }

    @Test(expected = ResourceExistsException.class)
    public void testCreateExists(){
        UserDto newUser = getUpdateUserDto();

        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        userService.create(newUser);
    }

    @Test
    public void testUpdateUser(){

        UserDto newUserDto = getUpdateUserDto();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenAnswer(i -> Optional.of(getFirstUser()));

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(User.class), Mockito.eq(UserDto.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            return new UserDto(
                    u.getId(),
                    u.getEmail(),
                    null,
                    u.getFirstName(),
                    u.getLastName(),
                    u.getLastPasswordChange(),
                    u.isVerified(),
                    (Set<Authority>) u.getAuthorities()
            );
        });

        UserDto updated = userService.update(newUserDto);
        assertEquals(updated.getEmail(), newUserDto.getEmail());
        assertEquals(updated.getFirstName(), newUserDto.getFirstName());
        assertEquals(updated.getLastName(), newUserDto.getLastName());
        assertEquals(updated.isVerified(), newUserDto.isVerified());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNotFound(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenAnswer(i -> Optional.empty());
        UserDto newUserDto = getNewUserDto();
        userService.update(newUserDto);
    }


    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteNotExists(){

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.doNothing().when(userRepository).delete(Mockito.any());
        userService.delete(Mockito.anyLong());
    }
}
