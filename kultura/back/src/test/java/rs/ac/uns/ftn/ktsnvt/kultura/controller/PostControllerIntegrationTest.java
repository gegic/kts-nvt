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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
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

    @Before
    public void login() {

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new LoginDto("moderator@mail.com", "admin123"),
                        String.class);
        JsonNode parent= null;
        try {
            parent = new ObjectMapper().readTree(responseEntity.getBody());
            accessToken = parent.path("token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenGetPostsByCulturalOfferingId(){
        Pageable pageRequest = PageRequest.of(0, 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<PostDto>> responseType = new ParameterizedTypeReference<HelperPage<PostDto>>() {};

        ResponseEntity<HelperPage<PostDto>> response = restTemplate.exchange(
                "/api/posts/cultural-offering/" + CulturalOfferingConstants.EXISTING_ID1
                , HttpMethod.GET, httpEntity, responseType);

        List<PostDto> posts = response.getBody().getContent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PostConstants.EXISTING_ID2, posts.get(0).getId());
        assertEquals(PostConstants.DB_COUNT, posts.size());
    }

    @Test
    public void whenCreatePost() {
        PostDto post = createTestPostDto();

        Pageable pageRequest = PageRequest.of(0, 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(post, headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts", HttpMethod.POST, httpEntity, PostDto.class);

        PostDto newPost = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newPost.getContent(), post.getContent());
    }

    @Test
    public void whenUpdatePost() {
        PostDto post = new PostDto();
        post.setId(PostConstants.EXISTING_ID1);
        post.setContent(PostConstants.TEST_CONTENT);

        Pageable pageRequest = PageRequest.of(0, 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(post, headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts", HttpMethod.PUT, httpEntity, PostDto.class);

        PostDto newPost = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newPost.getContent(), post.getContent());
    }

    @Test
    public void whenUpdatePostReturnResourceNotFoundException() {
        PostDto post = new PostDto();
        post.setId(33L); //nepostojeci ID
        post.setContent(PostConstants.TEST_CONTENT);

        Pageable pageRequest = PageRequest.of(0, 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(post, headers);

        ResponseEntity<PostDto> response = restTemplate.exchange(
                "/api/posts", HttpMethod.PUT, httpEntity, PostDto.class);

        PostDto newPost = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    private PostDto createTestPostDto() {
        PostDto postDto = new PostDto();

        postDto.setContent(PostConstants.TEST_CONTENT);
        postDto.setCulturalOfferingId(PostConstants.TEST_CULTURAL_OFFERING_ID);

        return postDto;
    }

}
