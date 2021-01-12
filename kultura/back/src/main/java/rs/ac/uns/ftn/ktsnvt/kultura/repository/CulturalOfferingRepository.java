package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import java.util.List;


@Repository
public interface CulturalOfferingRepository extends JpaRepository<CulturalOffering, Long> {
    @Query("select co from CulturalOffering co where (co.latitude between :latitudeStart and :latitudeEnd) and " +
                                                     "(co.longitude between :longitudeStart and :longitudeEnd)")
    List<CulturalOffering> findByBounds(float latitudeStart,
                                        float latitudeEnd,
                                        float longitudeStart,
                                        float longitudeEnd);

    @Query("select co from CulturalOffering co where lower(co.name) like lower(concat('%', :searchQuery,'%'))" +
            "and co.overallRating >= :ratingMin and co.overallRating <= :ratingMax")
    Page<CulturalOffering> searchAll(Pageable p, String searchQuery, float ratingMin, float ratingMax);
}
