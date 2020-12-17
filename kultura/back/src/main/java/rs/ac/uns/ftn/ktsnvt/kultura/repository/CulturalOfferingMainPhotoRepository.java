package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;

import java.util.List;

@Repository
public interface CulturalOfferingMainPhotoRepository extends JpaRepository<CulturalOfferingMainPhoto, Long> {

    void deleteAllByCulturalOfferingIsNull();

}
