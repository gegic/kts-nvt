package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.ReviewConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReviewRepositoryIntegrationTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    public void findReviewsSearchedByCulturalOfferingWhenExist(){
        Page<Review> reviewPage =reviewRepository.findAllByCulturalOfferingId(ReviewConstants.EXISTING_CULTURAL_OFFERING_ID, Pageable.unpaged());

        assertEquals(Long.valueOf(reviewPage.getContent().get(0).getId()),ReviewConstants.EXISTING_ID);

        assertEquals(reviewPage.getContent().get(0).getComment(), ReviewConstants.EXISTING_COMMENT);
    }

    @Test
    public void findReviewsSearchedByCulturalOfferingWhenNotExist(){
        long nonExistId = -5656L;
        Page<Review> reviewPage =reviewRepository.findAllByCulturalOfferingId(nonExistId, Pageable.unpaged());

        assertTrue(reviewPage.isEmpty());
    }

    @Test
    public void findAndGroupByRating() {
        long culturalOfferingId = 2;

        List<ReviewNumbersDto> reviewNumbersList = reviewRepository.findAndGroupByRating(culturalOfferingId);
        ReviewNumbersDto culturalRating = reviewNumbersList.get(0);

        assertEquals(1, reviewNumbersList.size());
        assertEquals(1, culturalRating.getNumReviews());
        assertEquals(3, culturalRating.getRating());
    }
}
