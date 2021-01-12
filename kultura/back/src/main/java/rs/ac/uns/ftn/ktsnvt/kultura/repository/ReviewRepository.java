package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByCulturalOfferingId(long culturalOfferingId, Pageable p);
    List<Review> findAllByCulturalOfferingId(long culturalOfferingId);
    @Query("select new rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewNumbersDto(r.rating, count(r)) from Review r where r.culturalOffering.id = :culturalOfferingId group by r.rating")
    List<ReviewNumbersDto> findAndGroupByRating(long culturalOfferingId);

    Optional<Review> findByCulturalOfferingIdAndUserId(long culturalOfferingId, long userId);
}
