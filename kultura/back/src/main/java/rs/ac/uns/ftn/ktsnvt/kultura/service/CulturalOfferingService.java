package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CulturalOfferingService {

    private final CulturalOfferingRepository culturalOfferingRepository;
    private final CulturalOfferingMainPhotoService mainPhotoService;
    private final CulturalOfferingPhotoService culturalOfferingPhotoService;
    private final ReviewService reviewService;
    private final Mapper modelMapper;


    @Autowired
    public CulturalOfferingService(CulturalOfferingRepository culturalOfferingRepository,
                                   Mapper modelMapper,
                                   CulturalOfferingMainPhotoService mainPhotoService,
                                   CulturalOfferingPhotoService culturalOfferingPhotoService,
                                   ReviewService reviewService) {
        this.culturalOfferingRepository = culturalOfferingRepository;
        this.modelMapper = modelMapper;
        this.mainPhotoService = mainPhotoService;
        this.culturalOfferingPhotoService = culturalOfferingPhotoService;
        this.reviewService = reviewService;
    }


    public Page<CulturalOfferingDto> readAll(Pageable p,
                                             String searchQuery,
                                             float ratingMin,
                                             float ratingMax,
                                             boolean noReviews,
                                             long categoryId,
                                             long subcategoryId) {
        Page<CulturalOffering> found;
        if (subcategoryId != -1) {
            if (noReviews) {
                found = culturalOfferingRepository.searchAllNoReviews(p, searchQuery, ratingMin, ratingMax);
            } else {
                found = culturalOfferingRepository.searchAll(p, searchQuery, ratingMin, ratingMax);
            }
        } else if (categoryId != -1) {
            if (noReviews) {
                found = culturalOfferingRepository.searchAllNoReviews(p, searchQuery, ratingMin, ratingMax);
            } else {
                found = culturalOfferingRepository.searchAll(p, searchQuery, ratingMin, ratingMax);
            }
        } else {
            if (noReviews) {
                found = culturalOfferingRepository.searchAllNoReviews(p, searchQuery, ratingMin, ratingMax);
            } else {
                found = culturalOfferingRepository.searchAll(p, searchQuery, ratingMin, ratingMax);
            }
        }

        return found.map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class));
    }

    public Optional<CulturalOfferingDto> readById(long id) {
        return culturalOfferingRepository.findById(id).map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class));
    }

    @Transactional
    public CulturalOfferingDto create(CulturalOfferingDto c) {
        CulturalOffering culturalOffering = modelMapper.fromDto(c, CulturalOffering.class);

        if (c.getId() != null &&
                culturalOfferingRepository.existsById(c.getId())) throw new ResourceExistsException("The cultural offering you are trying to create already exists!");

//        CulturalOfferingMainPhoto photo = photoRepository.getOne(c.getPhotoId());
//        culturalOffering.setPhoto(photo);
        culturalOffering = culturalOfferingRepository.save(culturalOffering);

        return modelMapper.fromEntity(culturalOffering, CulturalOfferingDto.class);
    }

    @Transactional
    public CulturalOfferingDto update(CulturalOfferingDto c) {
        if (c.getId() == null) throw new NullPointerException();

        CulturalOffering toUpdate = culturalOfferingRepository.findById(c.getId())
                .orElseThrow(EntityNotFoundException::new);

        CulturalOffering updateWith = modelMapper.toExistingEntity(c, toUpdate);

        toUpdate.setName(updateWith.getName());
        toUpdate.setAddress(updateWith.getAddress());
        toUpdate.setLatitude(updateWith.getLatitude());
        toUpdate.setLongitude(updateWith.getLongitude());
        toUpdate.setBriefInfo(updateWith.getBriefInfo());
        toUpdate.setAdditionalInfo(updateWith.getAdditionalInfo());
        toUpdate.setSubcategory(toUpdate.getSubcategory());
        CulturalOfferingMainPhoto p = updateWith.getPhoto();
        CulturalOfferingMainPhoto photo = toUpdate.getPhoto();
        if (p.getId() != photo.getId()) {
            mainPhotoService.deletePhoto(photo);
            p.setCulturalOffering(toUpdate);
        }
        toUpdate = culturalOfferingRepository.save(toUpdate);

        return modelMapper.fromEntity(toUpdate, CulturalOfferingDto.class);
    }


    @Transactional
    public List<CulturalOfferingDto> findByBounds(float latitudeStart,
                                                  float latitudeEnd,
                                                  float longitudeStart,
                                                  float longitudeEnd) {
        return this.culturalOfferingRepository.findByBounds(latitudeStart, latitudeEnd, longitudeStart, longitudeEnd)
                .stream().map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class)).collect(Collectors.toList());
    }

    public void delete(long id) {
        CulturalOffering co = culturalOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cultural offering with given id not found."));

        mainPhotoService.deletePhoto(co.getPhoto());
        culturalOfferingPhotoService.deleteByCulturalOffering(co.getId());
        reviewService.deleteByCulturalOfferingId(co.getId());

        culturalOfferingRepository.deleteById(id);
    }
}
