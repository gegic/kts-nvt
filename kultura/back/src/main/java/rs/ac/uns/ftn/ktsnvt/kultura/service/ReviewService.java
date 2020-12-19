package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;

import java.util.Optional;


@Service
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final Mapper mapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, Mapper mapper) {
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
    }

    public Page<ReviewDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return reviewRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    public Optional<ReviewDto> readById(long id) {
        return reviewRepository.findById(id).map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    public ReviewDto save(ReviewDto p) {
        Review r = mapper.fromDto(p, Review.class);
        float overallRating = r.getCulturalOffering().getOverallRating();
        int numReviews = r.getCulturalOffering().getNumReviews();
        float ratingSum = overallRating * numReviews;
        overallRating = (ratingSum + p.getRating()) / ++numReviews;
        r.getCulturalOffering().setOverallRating(overallRating);
        r.getCulturalOffering().setNumReviews(numReviews);

        return mapper.fromEntity(reviewRepository.save(r), ReviewDto.class);
    }

    public void delete(long id) {
        reviewRepository.deleteById(id);
    }
}
