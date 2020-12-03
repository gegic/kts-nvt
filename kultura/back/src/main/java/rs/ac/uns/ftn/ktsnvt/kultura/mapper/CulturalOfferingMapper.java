package rs.ac.uns.ftn.ktsnvt.kultura.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityNotFoundException;

@Component
public class CulturalOfferingMapper {

    @Autowired
    CulturalOfferingRepository culturalOfferingRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    public CulturalOffering fromDto(CulturalOfferingDto c) {
        Subcategory s = subcategoryRepository.findById(c.getSubcategoryId()).orElseThrow(EntityNotFoundException::new);
        return new CulturalOffering(
                c.getId(),
                c.getName(),
                c.getBriefInfo(),
                c.getLongitude(),
                c.getLatitude(),
                c.getAddress(),
                null,
                c.getOverallRating(),
                c.getNumReviews(),
                c.getLastChange(),
                c.getAdditionalInfo(),
                s,
                null,
                null,
                null,
                null
        );
    }

    public CulturalOfferingDto fromEntity(CulturalOffering c) {
        return new CulturalOfferingDto(
                c.getId(),
                c.getName(),
                c.getBriefInfo(),
                c.getLatitude(),
                c.getLongitude(),
                c.getAddress(),
                c.getPhoto() == null ? 0 : c.getPhoto().getId(), // TODO CHANGE THIS LINE
                c.getOverallRating(),
                c.getNumReviews(),
                c.getLastChange()
                ,
                c.getAdditionalInfo(),
                c.getSubcategory().getId(),
                c.getSubcategory().getName()
        );
    }
}
