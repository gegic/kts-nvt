package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;

import javax.persistence.MapsId;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReviewServiceUnitTest {

    @Autowired
    ReviewService reviewService;

    @MockBean
    ReviewRepository reviewRepository;

    @MockBean
    CulturalOfferingRepository culturalOfferingRepository;

    @MockBean
    ReviewPhotoRepository photoRepository;

    @MockBean
    Mapper mapper;

    @Before
    public void setupMapper() {
        Mockito.when(mapper.fromDto(Mockito.any(ReviewDto.class), Mockito.eq(Review.class))).thenAnswer(i -> {
            ReviewDto dto = i.getArgument(0);
            Review review = new Review();
            if (dto.getId() != null) {
                review.setId(dto.getId());
            }
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
            User u = new User();
            u.setId(dto.getUserId());
            CulturalOffering co = new CulturalOffering();
            co.setId(dto.getCulturalOfferingId());

            review.setUser(u);
            review.setCulturalOffering(co);
            return review;
        });

        Mockito.when(mapper.fromEntity(Mockito.any(Review.class), Mockito.eq(ReviewDto.class))).thenAnswer(i -> {
            Review review = i.getArgument(0);
            ReviewDto dto = new ReviewDto();

            dto.setId(review.getId());
            dto.setComment(review.getComment());
            dto.setTimeAdded(review.getTimeAdded());
            if (review.getCulturalOffering() != null) {
                dto.setCulturalOfferingId(review.getCulturalOffering().getId());
            }
            if (review.getUser() != null) {
                dto.setUserId(review.getUser().getId());
            }

            return dto;
        });

        Mockito.when(mapper.toExistingEntity(Mockito.any(ReviewDto.class), Mockito.any(Review.class))).thenAnswer(i -> {
            Review review = i.getArgument(1);
            ReviewDto dto = i.getArgument(0);
            if (dto.getId() != null) {
                review.setId(dto.getId());
            }
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
            User u = new User();
            u.setId(dto.getUserId());
            CulturalOffering co = new CulturalOffering();
            co.setId(dto.getCulturalOfferingId());

            review.setUser(u);
            review.setCulturalOffering(co);
            return review;
        });
    }



    @Test
    public void readAllByCulturalOfferingId() {
        Review r = new Review();
        User u = new User();
        u.setId(1L);
        r.setUser(u);
        CulturalOffering co = new CulturalOffering();
        co.setId(1L);
        r.setCulturalOffering(co);
        r.setComment("K");
        r.setRating(3);
        List<Review> reviews = new ArrayList<>();
        reviews.add(r);
        Mockito.when(reviewRepository.findAllByCulturalOfferingId(1L, PageRequest.of(0, Integer.MAX_VALUE)))
                .thenReturn(new PageImpl<Review>(reviews, PageRequest.of(0, Integer.MAX_VALUE), 1));

        Page<ReviewDto> dtos = reviewService.readAllByCulturalOfferingId(1L, PageRequest.of(0, Integer.MAX_VALUE));

        assertEquals(1, dtos.getTotalElements());
        assertEquals(1, dtos.getContent().size());
    }

    @Test
    public void readById() {
        Review r = new Review();
        User u = new User();
        u.setId(1L);
        r.setUser(u);
        CulturalOffering co = new CulturalOffering();
        co.setId(1L);
        r.setCulturalOffering(co);
        r.setComment("K");
        r.setRating(3);
        r.setId(1L);
        Mockito.when(reviewRepository.findById(1L)).thenReturn(java.util.Optional.of(r));

        ReviewDto dto = reviewService.readById(1L).get();

        assertEquals(1L, dto.getId().longValue());
    }

    @Test
    public void findAndGroupByRating() {

        List<ReviewNumbersDto> stats = new ArrayList<>();
        stats.add(new ReviewNumbersDto(3, 4));

        Mockito.when(reviewRepository.findAndGroupByRating(Mockito.anyLong())).thenReturn(stats);

        List<ReviewNumbersDto> culStats = reviewService.findAndGroupByRating(1);

        assertEquals(1, culStats.size());
        assertEquals(3, culStats.get(0).getRating());
        assertEquals(4, culStats.get(0).getNumReviews());
    }

    @Test
    public void findByCulturalOfferingAndUser() {
        Review r = new Review();
        User u = new User();
        u.setId(1L);
        r.setUser(u);
        CulturalOffering co = new CulturalOffering();
        co.setId(1L);
        r.setCulturalOffering(co);
        r.setComment("K");
        r.setRating(3);
        r.setId(1L);

        Mockito.when(reviewRepository.findByCulturalOfferingIdAndUserId(1L, 1L)).thenReturn(java.util.Optional.of(r));

        ReviewDto dto = reviewService.findByCulturalOfferingAndUser(1L, 1L).get();

        assertEquals(r.getId(), dto.getId().longValue());
    }

    @Test
    public void deleteByCulturalOfferingId() {
        Review r = new Review();
        User u = new User();
        u.setId(1L);
        r.setUser(u);
        CulturalOffering co = new CulturalOffering();
        co.setId(1L);
        r.setCulturalOffering(co);
        r.setComment("K");
        r.setRating(3);
        r.setId(1L);
        Review r1 = new Review();
        User u1 = new User();
        u.setId(1L);
        r1.setUser(u1);
        r1.setCulturalOffering(co);
        r1.setComment("K");
        r1.setRating(3);
        r1.setId(2L);
        List<Review> reviews = new ArrayList<>();
        reviews.add(r);
        reviews.add(r1);

        Mockito.when(reviewRepository.findAllByCulturalOfferingId(1L)).thenReturn(reviews);

        Mockito.doNothing().when(reviewRepository).deleteAll(Mockito.anyIterable());

        reviewService.deleteByCulturalOfferingId(1L);

        Mockito.verify(reviewRepository).deleteAll(Mockito.anyIterable());


    }
}