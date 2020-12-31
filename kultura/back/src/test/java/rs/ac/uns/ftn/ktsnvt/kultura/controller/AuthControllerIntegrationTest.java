package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.StringDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ErrorMessage;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.TokenUtils;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;


@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AuthControllerIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;
    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

    @Test
    public void testLogin() {

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new LoginDto(ADMIN_EMAIL, ADMIN_PASSWORD),
                        String.class);
        JsonNode parent= null;
        String token = "";
        assertNotNull(responseEntity.getBody());
        try {
            parent = new ObjectMapper().readTree(responseEntity.getBody());
            token = parent.path("token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String email = TokenUtils.getUsernameFromToken(token);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ADMIN_EMAIL, email);
    }

    @Test
    public void testExistsByEmail() {

        ResponseEntity<StringDto> response = restTemplate.getForEntity("/auth/exists/email/" + ADMIN_EMAIL,
                StringDto.class);

        String value = response.getBody().getValue();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ADMIN_FULL_NAME, value);
    }

    @Test
    public void testExistsByEmailNonExistent() {

        ResponseEntity<StringDto> response = restTemplate.getForEntity("/auth/exists/email/" + NON_EXISTENT_EMAIL,
                StringDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testExistsById() {
        ResponseEntity<StringDto> response = restTemplate.getForEntity("/auth/exists/verify/id/" + UNVERIFIED_ID2,
                StringDto.class);

        String value = response.getBody().getValue();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testExistsByIdNonExistent() {
        ResponseEntity<StringDto> response = restTemplate.getForEntity("/auth/exists/verify/id/" + NON_EXISTENT_ID,
                StringDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRegister() throws Exception {
        UserDto newUser = createUserDto();

        ResponseEntity<Void> response = restTemplate.postForEntity("/auth/register/",
                newUser, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // remove
        UserDto userDto = userService.findByEmail(newUser.getEmail()).orElse(null);
        if (userDto != null) {
            userService.delete(userDto.getId());
        }
    }

    @Test
    public void testRegisterEmailExists() {
        UserDto newUser = createUserDto();
        newUser.setEmail(ADMIN_EMAIL);

        ResponseEntity<Void> response = restTemplate.postForEntity("/auth/register/",
                newUser, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testVerify() {
        ResponseEntity<UserDto> response = restTemplate.getForEntity("/auth/verify/" + UNVERIFIED_ID, UserDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isVerified());

        User u = userRepository.findById(response.getBody().getId()).orElse(null);
        if (u != null) {
            u.setVerified(false);
            userRepository.save(u);
        }

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
