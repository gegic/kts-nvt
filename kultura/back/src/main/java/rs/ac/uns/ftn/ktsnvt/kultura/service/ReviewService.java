package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewSummaryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;


import java.util.Map;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final Mapper mapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, Mapper mapper) {
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Page<ReviewDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return reviewRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    public Map<Integer, Long> getSummary(long culturalOfferingId){
        Map<Integer, Long> ratings = new TreeMap<>();
        for(int i = 1;i<6;i++){
            long count = reviewRepository.getReviewsSize(i, culturalOfferingId);
            ratings.put(i, count);
        }
        return ratings;
    }


    public Optional<ReviewDto> readById(long id) {
        return reviewRepository.findById(id).map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    @Transactional
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
