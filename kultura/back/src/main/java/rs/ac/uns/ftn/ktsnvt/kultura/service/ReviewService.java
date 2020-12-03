package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;

import java.util.Optional;


@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return reviewRepository.findAllByCulturalOfferingId(culturalOfferingId, p);
    }

    public Optional<Review> readById(long id) {
        return reviewRepository.findById(id);
    }

    public Review save(Review p) {
//        if(p.getCulturalOffering()==null){
//            throw new Exception("Added review has no reviewwd cultural offering.");
//        }
        return reviewRepository.save(p);
    }

    public void delete(long id) {
        reviewRepository.deleteById(id);
    }
}
