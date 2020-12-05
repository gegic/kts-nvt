package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;



@Repository
public interface PhotoRepository extends JpaRepository<CulturalOfferingPhoto, Long> {
    Page<CulturalOfferingPhoto> findAllByCulturalOfferingId(long culturalOfferingId, Pageable p);

}
