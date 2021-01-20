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
import org.springframework.core.Constants;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.PostConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.ReviewConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.ReviewService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ReviewControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CulturalOfferingRepository culturalOfferingRepository;
    @Autowired
    private CulturalOfferingService culturalOfferingService;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

    @Test
    public void whenReadByCulturalOfferingId(){
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<ReviewDto>> responseType = new ParameterizedTypeReference<HelperPage<ReviewDto>>() {};

        ResponseEntity<HelperPage<ReviewDto>> response = restTemplate.exchange(
                "/api/reviews/cultural-offering/" + CulturalOfferingConstants.EXISTING_ID2
                , HttpMethod.GET, httpEntity, responseType);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<ReviewDto> reviews = response.getBody().getContent();
        reviews.forEach(r->assertEquals(r.getCulturalOfferingId(), CulturalOfferingConstants.EXISTING_ID2));
        this.accessToken = null;
    }

    @Test
    public void whenReadByCulturalOfferingIdNonExisting(){
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<ReviewDto>> responseType = new ParameterizedTypeReference<HelperPage<ReviewDto>>() {};

        ResponseEntity<HelperPage<ReviewDto>> response = restTemplate.exchange(
                "/api/reviews/cultural-offering/" + CulturalOfferingConstants.TEST_ID1
                , HttpMethod.GET, httpEntity, responseType);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<ReviewDto> reviews = response.getBody().getContent();
        assertEquals(0, reviews.size());
        this.accessToken = null;
    }

    @Test
    public void whenReadById(){
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews/" + ReviewConstants.EXISTING_ID,
                HttpMethod.GET, httpEntity, ReviewDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReviewDto result = response.getBody();
        assertNotNull(result);
        assertEquals(ReviewConstants.EXISTING_ID, result.getId());

        this.accessToken = null;
    }

    @Test
    public void whenReadByUser(){
        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews/cultural-offering/" + ReviewConstants.EXISTING_CULTURAL_OFFERING_ID +
                        "/user/" + ReviewConstants.EXISTING_USER_ID,
                HttpMethod.GET, httpEntity, ReviewDto.class);

        ReviewDto result = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(result.getUserId(), ReviewConstants.EXISTING_USER_ID);
        assertEquals(result.getCulturalOfferingId(), ReviewConstants.EXISTING_CULTURAL_OFFERING_ID);

    }

    @Test
    public void whenReadByInvalidUser(){
        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews/cultural-offering/" + ReviewConstants.EXISTING_CULTURAL_OFFERING_ID +
                "/user/" + ReviewConstants.NON_EXISTING_USER_ID,
                HttpMethod.GET, httpEntity, ReviewDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenReadByInvalidCulturalOffering(){
        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews/cultural-offering/" + ReviewConstants.NON_EXISTING_CULTURAL_OFFERING_ID +
                        "/user/" + ReviewConstants.EXISTING_USER_ID,
                HttpMethod.GET, httpEntity, ReviewDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenReadByIdNotFound(){
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews/" + ReviewConstants.TEST_ID,
                HttpMethod.GET, httpEntity, ReviewDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ReviewDto result = response.getBody();
        assertNull(result);
        this.accessToken = null;
    }

    @Test
    @Transactional
    public void testCreate(){
        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);
        ReviewDto review = new ReviewDto();
        review.setUserId(3L);
        review.setCulturalOfferingId(1L);
        review.setRating(3);
        review.setComment("KOMENTARCIC");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(review, headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews", HttpMethod.POST, httpEntity, ReviewDto.class);

        ReviewDto createdReview = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(review.getComment(), createdReview.getComment());

        reviewService.delete(createdReview.getId());
    }

    @Test
    public void testDeleteByModerator(){
        ReviewDto review = createTestReviewDto();

        review = reviewService.create(review);
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ReviewDto> response = restTemplate.exchange(
                "/api/reviews/" + review.getId(),
                HttpMethod.DELETE, httpEntity, ReviewDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteByUser(){
        ReviewDto review = createTestReviewDto();

        review = reviewService.create(review);

        this.accessToken = LoginUtil.login(restTemplate, USER_EMAIL, USER_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/reviews/" + review.getId(),
                HttpMethod.DELETE, httpEntity, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }




    private ReviewDto createTestReviewDto(){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setComment(ReviewConstants.TEST_COMMENT);
        reviewDto.setRating(ReviewConstants.TEST_RATING);
        reviewDto.setCulturalOfferingId(ReviewConstants.TEST_CULTURAL_OFFERING_ID);
        reviewDto.setUserId(3L);

        return reviewDto;
    }
}
