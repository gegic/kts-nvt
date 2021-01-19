package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.model.ReviewPhoto;

import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReviewPhotoRepositoryTest {

    @Autowired
    ReviewPhotoRepository photoRepository;

    @Test
    public void getNullReview() {
        ReviewPhoto rp = new ReviewPhoto();
        rp.setToken("MOJTOKEN");
        rp = photoRepository.save(rp);

        List<ReviewPhoto> reviewPhotoList = photoRepository.getNullReview("MOJTOKEN");

        assertEquals(1, reviewPhotoList.size());
        assertEquals(rp.getToken(), reviewPhotoList.get(0).getToken());
        assertNull(reviewPhotoList.get(0).getReview());
    }
}