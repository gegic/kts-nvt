package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PhotoRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;

    public Page<Photo> readAllByCulturalOfferingId(UUID culturalOfferingId, Pageable p) {
        return photoRepository.findAllByCulturalOfferingId(culturalOfferingId, p);
    }

    public Optional<Photo> readById(UUID id) {
        return photoRepository.findById(id);
    }

    public Photo save(Photo p) {
        return photoRepository.save(p);
    }

    public void delete(UUID id) {
        photoRepository.deleteById(id);
    }
}
