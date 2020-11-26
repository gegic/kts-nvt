package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CulturalOfferingService {
    
    @Autowired
    private CulturalOfferingRepository culturalOfferingRepository;

    public Page<CulturalOffering> readAll(Pageable p) {
        return culturalOfferingRepository.findAll(p);
    }

    public Optional<CulturalOffering> readById(UUID id) {
        return culturalOfferingRepository.findById(id);
    }

    public CulturalOffering save(CulturalOffering c) {
        return culturalOfferingRepository.save(c);
    }

    public void delete(UUID id) {
        culturalOfferingRepository.deleteById(id);
    }
}
