package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class CulturalOfferingService {

    private final CulturalOfferingRepository culturalOfferingRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final Mapper modelMapper;

    @Autowired
    public CulturalOfferingService(CulturalOfferingRepository culturalOfferingRepository,
                                   SubcategoryRepository subcategoryRepository,
                                   Mapper modelMapper) {
        this.culturalOfferingRepository = culturalOfferingRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.modelMapper = modelMapper;
    }


    public Page<CulturalOfferingDto> readAll(Pageable p) {
        return culturalOfferingRepository.findAll(p).map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class));
    }

    public Optional<CulturalOfferingDto> readById(long id) {
        return culturalOfferingRepository.findById(id).map(co -> modelMapper.fromEntity(co, CulturalOfferingDto.class));
    }

    @Transactional
    public CulturalOfferingDto create(CulturalOfferingDto c) {
        if (culturalOfferingRepository.existsById(c.getId())) throw new EntityExistsException();
        return this.save(c);
    }

    @Transactional
    public CulturalOfferingDto update(CulturalOfferingDto c) {
        if (!culturalOfferingRepository.existsById(c.getId())) throw new EntityNotFoundException();

        return this.save(c);
    }

    @Transactional
    protected CulturalOfferingDto save(CulturalOfferingDto c) {
        CulturalOffering culturalOffering = modelMapper.fromDto(c, CulturalOffering.class);
        Subcategory s = subcategoryRepository.findById(c.getSubcategoryId()).orElseThrow(EntityNotFoundException::new);

        culturalOffering.setSubcategory(s);
        culturalOffering = culturalOfferingRepository.save(culturalOffering);
        subcategoryRepository.save(s);

        return modelMapper.fromEntity(culturalOffering, CulturalOfferingDto.class);
    }

    public void delete(long id) {
        culturalOfferingRepository.deleteById(id);
    }
}
