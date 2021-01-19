package rs.ac.uns.ftn.ktsnvt.kultura.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.PostConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PostService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.MODERATOR_EMAIL;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.MODERATOR_PASSWORD;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PostControllerIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @Autowired
    CulturalOfferingRepository culturalOfferingRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;


    @Test
    public void whenGetPostsByCulturalOfferingId(){
        Pageable pageRequest = PageRequest.of(0, 3);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<PostDto>> responseType = new ParameterizedTypeReference<HelperPage<PostDto>>() {};

        ResponseEntity<HelperPage<PostDto>> response = restTemplate.exchange(
                "/api/posts/cultural-offering/" + CulturalOfferingConstants.EXISTING_ID1,
                HttpMethod.GET, httpEntity, responseType);

        List<PostDto> posts = response.getBody().getContent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        posts.forEach(p->assertEquals(CulturalOfferingConstants.EXISTING_ID1, p.getCulturalOfferingId()));
        assertEquals(PostConstants.DB_COUNT, posts.size());

        this.accessToken = null;
    }

    @Test
    public void whenGetPostsByCulturalOfferingIdNonExistant(){
        Pageable pageRequest = PageRequest.of(0, 3);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<PostDto>> responseType = new ParameterizedTypeReference<HelperPage<PostDto>>() {};

        ResponseEntity<HelperPage<PostDto>> response = restTemplate.exchange(
                "/api/posts/cultural-offering/" + CulturalOfferingConstants.TEST_ID1,
                HttpMethod.GET, httpEntity, responseType);

        assertNotNull(response.getBody());
        List<PostDto> posts = response.getBody().getContent();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, posts.size());

        this.accessToken = null;
    }

    @Test
    public void getByIdTest(){

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts/" + PostConstants.EXISTING_ID1,
                HttpMethod.GET, httpEntity, PostDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PostDto resultDto = response.getBody();
        assertNotNull(resultDto);
        assertEquals(PostConstants.EXISTING_ID1, resultDto.getId());
        this.accessToken = null;
    }


    @Test
    public void getByIdNotFoundTest(){

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts/" + PostConstants.TEST_ID,
                HttpMethod.GET, httpEntity, PostDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        PostDto resultDto = response.getBody();
        assertNull(resultDto);
        this.accessToken = null;
    }

    @Test
    @Transactional
    public void whenCreatePost() {
        PostDto post = createTestPostDto();

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(post, headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts", HttpMethod.POST, httpEntity, PostDto.class);

        PostDto newPost = response.getBody();
        assertNotNull(newPost);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newPost.getContent(), post.getContent());

        this.accessToken = null;

        this.postService.delete(newPost.getId());
    }

    @Test
    @Transactional
    public void whenUpdatePost() throws Exception {
        PostDto oldValues = postService.readById(PostConstants.EXISTING_ID1).orElseThrow(() -> new Exception("Test invalid!"));

        PostDto post = new PostDto();
        post.setId(PostConstants.EXISTING_ID1);
        post.setContent(PostConstants.TEST_CONTENT);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(post, headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts", HttpMethod.PUT, httpEntity, PostDto.class);

        PostDto newPost = response.getBody();
        assertNotNull(newPost);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newPost.getContent(), post.getContent());

        this.accessToken = null;

        this.postService.update(oldValues);
    }

    @Test
    public void whenUpdatePostReturnResourceNotFoundException() {
        PostDto post = new PostDto();
        post.setId(33L); //nepostojeci ID
        post.setContent(PostConstants.TEST_CONTENT);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(post, headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts", HttpMethod.PUT, httpEntity, PostDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        this.accessToken = null;
    }



    private PostDto createTestPostDto() {
        PostDto postDto = new PostDto();

        postDto.setContent(PostConstants.TEST_CONTENT);
        postDto.setCulturalOfferingId(PostConstants.TEST_CULTURAL_OFFERING_ID);

        return postDto;
    }

}
