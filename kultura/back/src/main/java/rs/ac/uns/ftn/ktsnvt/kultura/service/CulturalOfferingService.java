package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CulturalOfferingService {

    private final CulturalOfferingRepository culturalOfferingRepository;
    private final CulturalOfferingMainPhotoRepository photoRepository;

    private final Mapper modelMapper;


    @Autowired
    public CulturalOfferingService(CulturalOfferingRepository culturalOfferingRepository,
                                   Mapper modelMapper,
                                   CulturalOfferingMainPhotoRepository photoRepository) {
        this.culturalOfferingRepository = culturalOfferingRepository;
        this.modelMapper = modelMapper;
        this.photoRepository = photoRepository;
    }


    public Page<CulturalOfferingDto> readAll(Pageable p) {
        return culturalOfferingRepository.findAll(p).map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class));
    }

    public Optional<CulturalOfferingDto> readById(long id) {
        return culturalOfferingRepository.findById(id).map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class));
    }

    @Transactional
    public CulturalOfferingDto create(CulturalOfferingDto c) {
        CulturalOffering culturalOffering = modelMapper.fromDto(c, CulturalOffering.class);

        if (c.getId() != null &&
                culturalOfferingRepository.existsById(c.getId())) throw new EntityExistsException();

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

        toUpdate = culturalOfferingRepository.save(updateWith);

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
        culturalOfferingRepository.deleteById(id);
    }
}
