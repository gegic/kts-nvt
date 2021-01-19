package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.model.ReviewPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class ReviewService {

    private final PhotosConfig photosConfig;
    private final ReviewRepository reviewRepository;
    private final CulturalOfferingRepository culturalOfferingRepository;
    private final ReviewPhotoRepository photoRepository;
    private final Mapper mapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         Mapper mapper,
                         ReviewPhotoRepository photoRepository,
                         CulturalOfferingRepository culturalOfferingRepository,
                         PhotosConfig photosConfig) {
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
        this.photoRepository = photoRepository;
        this.culturalOfferingRepository = culturalOfferingRepository;
        this.photosConfig = photosConfig;
    }

    @Transactional
    public Page<ReviewDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return reviewRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    public Optional<ReviewDto> readById(long id) {
        return reviewRepository.findById(id).map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    @Transactional
    public ReviewDto create(ReviewDto p) {
        Review r = mapper.fromDto(p, Review.class);
        float overallRating = r.getCulturalOffering().getOverallRating();
        int numReviews = r.getCulturalOffering().getNumReviews();
        float ratingSum = overallRating * numReviews;
        overallRating = (ratingSum + p.getRating()) / ++numReviews;
        r.getCulturalOffering().setOverallRating(overallRating);
        r.getCulturalOffering().setNumReviews(numReviews);

        Set<ReviewPhoto> newPhotos = r.getPhotos();
        for (ReviewPhoto photo : newPhotos) {
            photo.setReview(r);
        }
        r = reviewRepository.save(r);
        return mapper.fromEntity(r, ReviewDto.class);
    }

    @Transactional
    public ReviewDto update(ReviewDto p) {
        Review existing = this.reviewRepository.findById(p.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Review with the given id not found."));
        Review r = mapper.fromDto(p, Review.class);
        float overallRating = existing.getCulturalOffering().getOverallRating();
        int numReviews = existing.getCulturalOffering().getNumReviews();
        float newOverallRating = ((overallRating * numReviews) - existing.getRating() + p.getRating()) / numReviews;

        existing.getCulturalOffering().setOverallRating(newOverallRating);
        existing.setComment(r.getComment());
        existing.setRating(r.getRating());

        Set<ReviewPhoto> existingPhotos = existing.getPhotos();
        Set<ReviewPhoto> newPhotos = r.getPhotos();

        for (ReviewPhoto rp : existingPhotos) {
            if (newPhotos.stream().anyMatch(rph -> rph.getId() == rp.getId())) {
                continue;
            }
            new File(photosConfig.getPath() + "review/thumbnail/" + rp.getId() + ".png").delete();
            new File(photosConfig.getPath() + "review/" + rp.getId() + ".png").delete();

            photoRepository.delete(rp);
        }

        for (ReviewPhoto photo : newPhotos) {
            photo.setReview(existing);
        }

        return mapper.fromEntity(reviewRepository.save(r), ReviewDto.class);
    }

    @Transactional
    public void delete(long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Review with given id doesn't exist."));

        CulturalOffering culturalOffering = review.getCulturalOffering();

        int numReviews = culturalOffering.getNumReviews();
        float overallRating = culturalOffering.getOverallRating();

        int newNumReviews = numReviews - 1;
        float newOverallRating = (overallRating * numReviews - review.getRating()) / newNumReviews;
        if (Float.isNaN(newOverallRating)) {
            culturalOffering.setOverallRating(0f);
        } else {
            culturalOffering.setOverallRating(newOverallRating);
        }
        culturalOffering.setNumReviews(newNumReviews);
        culturalOffering.getReviews().remove(review);
        Set<ReviewPhoto> photos = review.getPhotos();

        for (ReviewPhoto photo : photos) {

            new File(photosConfig.getPath() + "review/thumbnail/" + photo.getId() + ".png").delete();
            new File(photosConfig.getPath() + "review/" + photo.getId() + ".png").delete();

            photoRepository.delete(photo);
        }

        reviewRepository.deleteById(id);
        culturalOfferingRepository.save(culturalOffering);
    }

    @Transactional
    public List<ReviewNumbersDto> findAndGroupByRating(long culturalOfferingId) {
        return this.reviewRepository.findAndGroupByRating(culturalOfferingId);
    }

    @Transactional
    public Optional<ReviewDto> findByCulturalOfferingAndUser(long culturalOfferingId,
                                                   long userId) {
        return this.reviewRepository.findByCulturalOfferingIdAndUserId(culturalOfferingId,
                userId).map(review -> mapper.fromEntity(review, ReviewDto.class));
    }

    @Transactional
    public void deleteByCulturalOfferingId(long culturalOfferingId) {

        List<Review> reviews = reviewRepository.findAllByCulturalOfferingId(culturalOfferingId);

        for (Review r : reviews) {
            Set<ReviewPhoto> photos = r.getPhotos();
            for (ReviewPhoto photo : photos) {
                new File(photosConfig.getPath() + "review/thumbnail/" + photo.getId() + ".png").delete();
                new File(photosConfig.getPath() + "review/" + photo.getId() + ".png").delete();
            }
            photoRepository.deleteAll(photos);
        }
        reviewRepository.deleteAll(reviews);

    }
}
