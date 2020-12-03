package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;



@Repository
public interface CulturalOfferingRepository extends JpaRepository<CulturalOffering, Long> {
}
