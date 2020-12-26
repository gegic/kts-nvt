package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.hibernate.dialect.DB2Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingServiceIntegrationTest {

    @Autowired
    private CulturalOfferingService culturalOfferingService;
    @Autowired
    private CulturalOfferingRepository culturalOfferingRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private Mapper modelMapper;


    @Test
    @Transactional
    public void testReadById(){
        Optional<CulturalOfferingDto> culturalOfferingDto = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1);

        assertEquals(CulturalOfferingConstants.EXISTING_ID1, culturalOfferingDto.get().getId());

    }

    @Test
    @Transactional
    public void testReadAll() {
        Pageable pageRequest = PageRequest.of(0, CulturalOfferingConstants.PAGE_SIZE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService.readAll(pageRequest);

        assertEquals(CulturalOfferingConstants.DB_COUNT, returnedCulturalOfferings.getContent().size());

    }

    @Test
    @Transactional
    public void testUpdate(){
        CulturalOfferingDto dbCulturalOffering = culturalOfferingService.readById
                (CulturalOfferingConstants.EXISTING_ID1).get();

        dbCulturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);

        dbCulturalOffering = culturalOfferingService.update(dbCulturalOffering);
        assertThat(dbCulturalOffering).isNotNull();

        //verify that database contains updated data
        dbCulturalOffering = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1).get();
        assertThat(dbCulturalOffering.getName()).isEqualTo(CulturalOfferingConstants.TEST_NAME1);

    }

    @Test
    @Transactional
    public void testCreate(){
        CulturalOfferingDto newCulturalOffering = getTestCulturalOfferingDto();

    }

    CulturalOfferingDto getTestCulturalOfferingDto(){
        CulturalOfferingDto culturalOfferingDto= new CulturalOfferingDto();
        culturalOfferingDto.setAddress(CulturalOfferingConstants.TEST_ADDRESS1);
        culturalOfferingDto.setBriefInfo(CulturalOfferingConstants.TEST_BRIEF_INFO1);
        culturalOfferingDto.setLatitude(CulturalOfferingConstants.TEST_LATITUDE1);
        culturalOfferingDto.setLongitude(CulturalOfferingConstants.TEST_LONGITUDE1);
        culturalOfferingDto.setSubcategoryId(CulturalOfferingConstants.TEST_SUBCATEGORY_ID1);
        culturalOfferingDto.setName(CulturalOfferingConstants.TEST_NAME1);
        culturalOfferingDto.setId(CulturalOfferingConstants.TEST_ID1);

        return culturalOfferingDto;
    }

}
