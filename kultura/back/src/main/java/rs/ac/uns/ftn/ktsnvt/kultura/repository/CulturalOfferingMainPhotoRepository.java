package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CulturalOfferingMainPhotoRepository extends JpaRepository<CulturalOfferingMainPhoto, Long> {

    @Query("select p from CulturalOfferingMainPhoto p where p.culturalOffering is null and p.token = :token")
    List<CulturalOfferingMainPhoto> getNullOffering(String token);
    @Query("select p from CulturalOfferingMainPhoto p where p.culturalOffering is null and p.culturalOffering.id = :culturalOfferingId")
    List<CulturalOfferingMainPhoto> getAllNullByCulturalOffering(long culturalOfferingId);
}
