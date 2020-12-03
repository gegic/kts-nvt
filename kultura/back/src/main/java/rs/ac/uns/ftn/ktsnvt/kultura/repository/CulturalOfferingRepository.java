package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CulturalOfferingRepository extends JpaRepository<CulturalOffering, Long> {
}
