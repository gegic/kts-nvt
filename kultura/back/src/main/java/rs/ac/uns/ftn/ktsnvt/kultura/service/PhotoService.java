package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Photo;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PhotoRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class PhotoService {


    private final PhotoRepository photoRepository;
    private final Mapper mapper;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, Mapper mapper) {
        this.photoRepository = photoRepository;
        this.mapper = mapper;
    }

    public Page<PhotoDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return photoRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(photo -> mapper.fromEntity(photo, PhotoDto.class));
    }

    public Optional<PhotoDto> readById(long id) {
        return photoRepository.findById(id).map(photo -> mapper.fromEntity(photo, PhotoDto.class));
    }

    public PhotoDto save(PhotoDto p) {
        return mapper.fromEntity(photoRepository.save(mapper.fromDto(p, Photo.class)), PhotoDto.class);
    }

    public void delete(long id) {
        photoRepository.deleteById(id);
    }
}
