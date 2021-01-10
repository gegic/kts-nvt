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

import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.MODERATOR_EMAIL;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.MODERATOR_PASSWORD;

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

//        List<ReviewDto> reviews = response.getBody().getContent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(ReviewConstants.EXISTING_ID, reviews.getId());
//        assertEquals(ReviewConstants.EXISTING_COMMENT, review.getComment());
//        assertEquals(ReviewConstants.EXISTING_RATING, review.getRating());

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

//        List<ReviewDto> reviews = response.getBody().getContent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(ReviewConstants.EXISTING_ID, reviews.getId());
//        assertEquals(ReviewConstants.EXISTING_COMMENT, review.getComment());
//        assertEquals(ReviewConstants.EXISTING_RATING, review.getRating());

        this.accessToken = null;
    }


    private ReviewDto createTestReviewDto(){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setComment(ReviewConstants.TEST_COMMENT);
        reviewDto.setRating(ReviewConstants.TEST_RATING);
        reviewDto.setCulturalOfferingId(ReviewConstants.TEST_CULTURAL_OFFERING_ID);
        reviewDto.setUserId(ReviewConstants.TEST_USER_ID);

        return reviewDto;
    }
}
