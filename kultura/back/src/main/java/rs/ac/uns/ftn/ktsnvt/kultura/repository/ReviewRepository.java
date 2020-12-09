package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;



@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByCulturalOfferingId(long culturalOfferingId, Pageable p);
}