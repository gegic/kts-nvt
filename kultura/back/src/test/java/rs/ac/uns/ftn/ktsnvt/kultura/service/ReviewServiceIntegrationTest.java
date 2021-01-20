package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.PAGE_SIZE;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReviewServiceIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    ReviewService reviewService;
    @Autowired
    CulturalOfferingRepository culturalOfferingRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    @Transactional
    public void testReadById(){
        ReviewDto review = reviewService.readById(1L).get();

        assertEquals(1, review.getId().longValue());
        assertEquals("Komentar", review.getComment());
        assertEquals(3, review.getRating().longValue());
    }

    @Test
    @Transactional
    public void testReadAllByCulturalOfferingId(){
        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<ReviewDto> returnedReviews = reviewService
                .readAllByCulturalOfferingId(ReviewConstants.EXISTING_CULTURAL_OFFERING_ID, pageRequest);

        assertEquals(ReviewConstants.DB_COUNT, returnedReviews.getTotalElements());
    }

    @Test
    @Transactional
    public void testCreate(){
        ReviewDto newReview = new ReviewDto();
        newReview.setComment("KOMENT");
        newReview.setCulturalOfferingId(1L);
        newReview.setUserId(3L);
        newReview.setRating(4);
        long oldDb = reviewRepository.count();

        ReviewDto createdReview = reviewService.create(newReview);

        assertNotNull(createdReview);

        long newDb = reviewRepository.count();
        assertEquals(oldDb + 1, newDb);
        assertEquals(newReview.getComment(), createdReview.getComment());
        assertEquals(newReview.getCulturalOfferingId(), createdReview.getCulturalOfferingId());
        assertEquals(newReview.getUserId(), createdReview.getUserId());

        Review r = reviewRepository.findById(createdReview.getId()).get();
        CulturalOffering culturalOffering = r.getCulturalOffering();
        culturalOffering.removeReview(r);
        culturalOffering.removeRating(r);
        culturalOfferingRepository.save(culturalOffering);
        reviewRepository.delete(r);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateReviewExists() {
        ReviewDto newReview = new ReviewDto();
        newReview.setComment("KOMENT");
        newReview.setCulturalOfferingId(2L);
        newReview.setUserId(3L);
        newReview.setRating(4);
        reviewService.create(newReview);
    }

    @Test
    @Transactional
    public void testUpdateReview() {
        Review review = reviewRepository.findById(1L).get();
        int oldRating = review.getRating();
        String oldComment = review.getComment();
        CulturalOffering culturalOffering = review.getCulturalOffering();
        ReviewDto toUpdate = new ReviewDto();
        toUpdate.setId(1L);
        toUpdate.setRating(4);
        toUpdate.setCulturalOfferingId(culturalOffering.getId());
        toUpdate.setUserId(review.getUser().getId());
        toUpdate.setComment("NOVI KOMENTAR");

        ReviewDto updated = reviewService.update(toUpdate);

        assertEquals(1L, updated.getId().longValue());
        assertEquals(toUpdate.getComment(), updated.getComment());

        culturalOffering.ratingChanged(4, oldRating);
        review.setRating(oldRating);
        review.setComment(oldComment);
        culturalOffering.reviewUpdated(review);
        culturalOfferingRepository.save(culturalOffering);
        reviewRepository.save(review);

    }

    @Test
    @Transactional
    public void testDeleteReview() {
        CulturalOffering culturalOffering = culturalOfferingRepository.getOne(1L);
        Review newReview = new Review();
        newReview.setComment("KOMENT");
        newReview.setRating(4);
        newReview.setCulturalOffering(culturalOfferingRepository.getOne(1L));
        float overallRating = culturalOffering.getOverallRating();
        int numReviews = culturalOffering.getNumReviews();
        float ratingSum = overallRating * numReviews;
        overallRating = (ratingSum + newReview.getRating()) / ++numReviews;
        culturalOffering.setOverallRating(overallRating);
        culturalOffering.setNumReviews(numReviews);
        newReview.setUser(userRepository.getOne(3L));
        Review r = reviewRepository.save(newReview);

        long oldDb = reviewRepository.count();

        reviewService.delete(r.getId());

        long newDb = reviewRepository.count();

        assertEquals(oldDb - 1, newDb);


    }

    @Test
    @Transactional
    public void testFindByCulturalOfferingAndUser() {

        ReviewDto dto = reviewService.findByCulturalOfferingAndUser(2L, 3L).get();

        assertEquals(2L, dto.getCulturalOfferingId().longValue());
        assertEquals(3L, dto.getUserId().longValue());
    }

    @Test
    public void testFindAndGroupByRating() {
        long culturalOfferingId = 2;

        List<ReviewNumbersDto> reviewNumbersList = reviewService.findAndGroupByRating(culturalOfferingId);
        ReviewNumbersDto culturalRating = reviewNumbersList.get(0);

        assertEquals(1, reviewNumbersList.size());
        assertEquals(1, culturalRating.getNumReviews());
        assertEquals(3, culturalRating.getRating());
    }

    @Test
    @Transactional
    public void testDeleteByCulturalOfferingId() {
        CulturalOffering culturalOffering = culturalOfferingRepository.getOne(1L);
        Review newReview = new Review();
        newReview.setComment("KOMENT");
        newReview.setRating(4);
        newReview.setCulturalOffering(culturalOfferingRepository.getOne(1L));
        float overallRating = culturalOffering.getOverallRating();
        int numReviews = culturalOffering.getNumReviews();
        float ratingSum = overallRating * numReviews;
        overallRating = (ratingSum + newReview.getRating()) / ++numReviews;
        culturalOffering.setOverallRating(overallRating);
        culturalOffering.setNumReviews(numReviews);
        newReview.setUser(userRepository.getOne(3L));
        Review r = reviewRepository.save(newReview);

        reviewService.deleteByCulturalOfferingId(1L);

        long newDb = reviewRepository.findAll().stream().filter(re -> re.getCulturalOffering().getId() == 1L).count();

        assertEquals(0, newDb);
    }

}
