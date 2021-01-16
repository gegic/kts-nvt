package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ErrorMessage;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;


@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class UsersControllerIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;
    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;
    @Test
    public void testGetModerators() {

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ParameterizedTypeReference<HelperPage<UserDto>> responseType = new ParameterizedTypeReference<HelperPage<UserDto>>() {};
        ResponseEntity<HelperPage<UserDto>> response = restTemplate.exchange("/api/users/moderators", HttpMethod.GET,
                httpEntity, responseType);

        UserDto moderator = response.getBody().getContent().get(0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MODERATOR_COUNT, response.getBody().getNumberOfElements());
        assertEquals(MODERATOR_EMAIL, moderator.getEmail());
        assertNull(moderator.getPassword());

        this.accessToken = null;
    }

    @Test
    public void testGetById() {
        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users/" + USER_ID, HttpMethod.GET,
                httpEntity, UserDto.class);

        UserDto moderator = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(USER_ID, moderator.getId().longValue());
        assertEquals(USER_EMAIL, moderator.getEmail());
        assertNull(moderator.getPassword());

        this.accessToken = null;
    }

    @Test
    public void testGetByIdNotFound() {
        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users/" + NON_EXISTENT_ID, HttpMethod.GET,
                httpEntity, UserDto.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        this.accessToken = null;
    }

    @Test
    @Transactional
    public void testPostNewUser() throws Exception {


        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        UserDto newUser = createUserDto();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users/", HttpMethod.POST,
                httpEntity, UserDto.class);

        UserDto createdUser = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
        assertEquals(newUser.getFirstName(), createdUser.getFirstName());
        assertEquals(newUser.getLastName(), createdUser.getLastName());
        assertTrue(createdUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().toLowerCase().contains("user")));
        assertNull(createdUser.getPassword());

        this.accessToken = null;

        userService.delete(createdUser.getId());
    }


    @Test
//    @Transactional
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void testPostNewUserBadPassword() throws Exception {
        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        UserDto newUser = createUserDto();
        newUser.setPassword("aaaa");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users/", HttpMethod.POST,
                httpEntity, UserDto.class);

        UserDto createdUser = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        this.accessToken = null;

        if (createdUser != null && createdUser.getId() != null){
            userService.delete(createdUser.getId());
        }

    }


    @Test
    public void testPostNewUserEmailExistsConflict() throws Exception {

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        UserDto newUser = createUserDto();

        newUser.setEmail(ADMIN_EMAIL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users/", HttpMethod.POST,
                httpEntity, UserDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        this.accessToken = null;

        newUser.setEmail(NEW_EMAIL);


        UserDto createdUser = response.getBody();

        if (createdUser != null && createdUser.getId() != null){
            userService.delete(createdUser.getId());
        }
    }

    @Test
    @Transactional
    public void testPostNewModerator() throws Exception {
        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        UserDto newUser = createUserDto();
        newUser.setEmail(NEW_MODERATOR_EMAIL);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users/moderator", HttpMethod.POST,
                httpEntity, UserDto.class);

        UserDto createdUser = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser.getEmail(), createdUser.getEmail());
        assertEquals(newUser.getFirstName(), createdUser.getFirstName());
        assertEquals(newUser.getLastName(), createdUser.getLastName());
        assertTrue(createdUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().toLowerCase().contains("moderator")));
        assertNull(createdUser.getPassword());

        this.accessToken = null;

        userService.delete(createdUser.getId());
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        UserDto oldValues = userService.findById(USER_ID).orElseThrow(() -> new Exception("Test invalid!"));


        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);
        UserDto u = createUpdateUserDto();
        u.setId(USER_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(u, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users", HttpMethod.PUT,
                httpEntity, UserDto.class);

        UserDto updatedUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(u.getEmail(), updatedUser.getEmail());
        assertEquals(u.getFirstName(), updatedUser.getFirstName());
        assertEquals(u.getLastName(), updatedUser.getLastName());
        assertNull(updatedUser.getPassword());

        this.accessToken = null;

        userService.update(oldValues);
    }


    @Test
//    @Transactional
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void testUpdateUserAsUser() throws Exception {
//        UserDto oldValues = userService.findById(USER_ID).orElseThrow(() -> new Exception("Test invalid!"));

        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);
        UserDto u = createUpdateUserDto();
        u.setEmail(UserConstants.NEW_USER_EMAIL);
        u.setId(USER_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(u, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users", HttpMethod.PUT,
                httpEntity, UserDto.class);

        UserDto updatedUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(updatedUser);
        assertEquals(u.getEmail(), updatedUser.getEmail());
        assertEquals(u.getFirstName(), updatedUser.getFirstName());
        assertEquals(u.getLastName(), updatedUser.getLastName());
        assertNull(updatedUser.getPassword());

        this.accessToken = null;

//        userService.update(oldValues);
    }
    @Test
//    @Transactional
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void testUpdateAdminAsAdmin() throws Exception {
//        UserDto oldValues = userService.findById(ADMIN_ID).orElseThrow(() -> new Exception("Test invalid!"));

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);
        UserDto u = createUpdateUserDto();
        u.setId(ADMIN_ID);
        u.setEmail(NEW_ADMIN_EMAIL);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(u, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users", HttpMethod.PUT,
                httpEntity, UserDto.class);

        UserDto updatedUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(updatedUser);
        assertEquals(u.getEmail(), updatedUser.getEmail());
        assertEquals(u.getFirstName(), updatedUser.getFirstName());
        assertEquals(u.getLastName(), updatedUser.getLastName());
        assertNull(updatedUser.getPassword());

        this.accessToken = null;

//        userService.update(oldValues);
    }

    @Test
//    @Transactional
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void testUpdateModeratorAsModerator() throws Exception {
//        UserDto oldValues = userService.findById(MODERATOR_ID).orElseThrow(() -> new Exception("Test invalid!"));

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);
        UserDto u = createUserDto();
        u.setId(MODERATOR_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(u, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users", HttpMethod.PUT,
                httpEntity, UserDto.class);

        UserDto updatedUser = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(updatedUser);
        assertEquals(u.getEmail(), updatedUser.getEmail());
        assertEquals(u.getFirstName(), updatedUser.getFirstName());
        assertEquals(u.getLastName(), updatedUser.getLastName());
        assertNull(updatedUser.getPassword());

        this.accessToken = null;

//        userService.update(oldValues);
    }

    @Test
//    @Transactional
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void testUpdateDifferentUserAsUser() throws Exception {
        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);
        UserDto u = createUpdateUserDto();
        u.setId(ADMIN_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(u, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users", HttpMethod.PUT,
                httpEntity, UserDto.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        this.accessToken = null;
    }
    @Test
//    @Transactional
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void testUpdateNonExistent() throws Exception {
        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);
        UserDto u = createUpdateUserDto();
        u.setId(NON_EXISTENT_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(u, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange("/api/users", HttpMethod.PUT,
                httpEntity, UserDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        this.accessToken = null;

        UserDto createdUser = response.getBody();
        if (createdUser != null && createdUser.getId() != null){
            userService.delete(createdUser.getId());
        }
    }

    private UserDto createUserDto() {
        return  new UserDto(
                null,
                NEW_EMAIL,
                "Admin123",
                NEW_FIRST_NAME,
                "last name",
                LocalDateTime.now(),
                true,
                null
        );
    }
    private UserDto createUpdateUserDto() {
        return  new UserDto(
                null,
                NEW_EMAIL,
                null,
                NEW_FIRST_NAME,
                "last name",
                LocalDateTime.now(),
                true,
                null
        );
    }
    private UserDto createUpdatePasswordUserDto() {
        return  new UserDto(
                null,
                null,
                "Admin123",
                null,
                null,
                null,
                true,
                null
        );
    }
}
