package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.ReviewConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
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
}
