package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingRepositoryIntegrationTest {

    @Autowired
    CulturalOfferingRepository culturalOfferingRepository;

    @Test
    public void findByIdWhenExist(){
        Optional<CulturalOffering> culturalOffering   = culturalOfferingRepository.findById(CulturalOfferingConstants.EXISTING_ID1);
        assertTrue(culturalOffering.isPresent());
    }

    @Test
    public void findByIdWhenNotExist(){
        long nonExistId = -5656L;
        Optional<CulturalOffering> culturalOffering  = culturalOfferingRepository.findById(nonExistId);
        assertFalse(culturalOffering.isPresent());
    }

    @Test
    public void findByBoundsWhenExits(){
        List<CulturalOffering> culturalOfferingList = culturalOfferingRepository.findByBounds(
                CulturalOfferingConstants.EXISTING_LATITUDE1-5,
                CulturalOfferingConstants.EXISTING_LATITUDE1+5,
                CulturalOfferingConstants.EXISTING_LONGITUDE1-5,
                CulturalOfferingConstants.EXISTING_LONGITUDE1+5
        );
        assertFalse(culturalOfferingList.stream().anyMatch(culturalOffering ->
                culturalOffering.getLatitude()> CulturalOfferingConstants.EXISTING_LATITUDE1+5 ||
                        culturalOffering.getLatitude()< CulturalOfferingConstants.EXISTING_LATITUDE1-5 ));

        assertFalse(culturalOfferingList.stream().anyMatch(culturalOffering ->
                culturalOffering.getLongitude()> CulturalOfferingConstants.EXISTING_LONGITUDE1+5 ||
                        culturalOffering.getLongitude()< CulturalOfferingConstants.EXISTING_LONGITUDE1-5 ));
    }

    public void findByBoundsWhenNotExits(){
        List<CulturalOffering> culturalOfferingList = culturalOfferingRepository.findByBounds(
                CulturalOfferingConstants.EXISTING_LATITUDE1-10000,
                CulturalOfferingConstants.EXISTING_LATITUDE1-10005,
                CulturalOfferingConstants.EXISTING_LONGITUDE1-10000,
                CulturalOfferingConstants.EXISTING_LONGITUDE1-10005
        );

        assertTrue(culturalOfferingList.isEmpty());
    }
}
