package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.ReviewConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.PAGE_SIZE;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ReviewServiceIntegrationTest {


    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    ReviewService reviewService;


    private ReviewDto createTestReviewDto(){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setComment(ReviewConstants.TEST_COMMENT);
        reviewDto.setRating(ReviewConstants.TEST_RATING);
        reviewDto.setCulturalOfferingId(ReviewConstants.TEST_CULTURAL_OFFERING_ID);
        reviewDto.setUserId(ReviewConstants.TEST_USER_ID);

        return reviewDto;
    }

    @Test
    @Transactional
    public void testReadById(){
        ReviewDto review = reviewService.readById(ReviewConstants.EXISTING_ID).get();

        assertEquals(ReviewConstants.EXISTING_ID, review.getId());
        assertEquals(ReviewConstants.EXISTING_COMMENT, review.getComment());
        assertEquals(ReviewConstants.EXISTING_RATING, review.getRating());
    }

    @Test
    @Transactional
    public void testReadAllByCulturalOfferingId(){
        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        Page<ReviewDto> returnedReviews = reviewService
                .readAllByCulturalOfferingId(ReviewConstants.EXISTING_CULTURAL_OFFERING_ID, pageRequest);

        assertEquals(ReviewConstants.DB_COUNT, returnedReviews.getContent().size());
    }

    @Test
    //@Rollback(true)
    public void testSave(){
        ReviewDto newReview = createTestReviewDto();

        ReviewDto createdReview = reviewService.save(newReview);

        assertThat(createdReview).isNotNull();

        // Validate that new category is in the database
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).hasSize(ReviewConstants.DB_COUNT + 1);
        assertEquals(createdReview.getId(), ReviewConstants.TEST_ID);
        assertEquals(createdReview.getComment(), newReview.getComment());
        assertEquals(createdReview.getCulturalOfferingId(), newReview.getCulturalOfferingId());
        assertEquals(createdReview.getRating(), newReview.getRating());

        reviewService.delete(createdReview.getId());
    }




}
