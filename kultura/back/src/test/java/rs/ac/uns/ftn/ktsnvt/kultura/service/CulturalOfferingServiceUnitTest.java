package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;
import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CulturalOfferingServiceUnitTest {

    @Autowired
    private CulturalOfferingService culturalOfferingService;
    @MockBean
    private CulturalOfferingRepository culturalOfferingRepository;
    @MockBean
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private Mapper modelMapper;

    @Before
    public void setUp() {
        Mockito.when(culturalOfferingRepository.findById(null)).thenReturn(null);

        Mockito.when(culturalOfferingRepository.existsById(CulturalOfferingConstants.TEST_ID1))
                .thenReturn(false);

        Mockito.when(culturalOfferingRepository.existsById(CulturalOfferingConstants.EXISTING_ID1))
                .thenReturn(true);

        Mockito.when(culturalOfferingRepository.findById(CulturalOfferingConstants.EXISTING_ID1))
                .thenReturn(java.util.Optional.of(getExistingCulturalOffering()));

        Mockito.when(culturalOfferingRepository.save(getExistingCulturalOffering()))
                .thenReturn(getExistingCulturalOffering());

        Mockito.when(culturalOfferingRepository.save(getTestCulturalOffering()))
                .thenReturn(getTestCulturalOffering());
    }

    CulturalOffering getTestCulturalOffering(){
        CulturalOffering culturalOffering= new CulturalOffering();
        culturalOffering.setAddress(CulturalOfferingConstants.TEST_ADDRESS1);
        culturalOffering.setBriefInfo(CulturalOfferingConstants.TEST_BRIEF_INFO1);
        culturalOffering.setLatitude(CulturalOfferingConstants.TEST_LATITUDE1);
        culturalOffering.setLongitude(CulturalOfferingConstants.TEST_LONGITUDE1);
        Subcategory subcategory = new Subcategory();
        subcategory.setId(CulturalOfferingConstants.TEST_SUBCATEGORY_ID1);
        culturalOffering.setSubcategory(subcategory);
        culturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);
        culturalOffering.setId(CulturalOfferingConstants.TEST_ID1);

        return culturalOffering;
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

    CulturalOffering getExistingCulturalOffering(){
        CulturalOffering culturalOffering = new CulturalOffering();
        culturalOffering.setAddress(CulturalOfferingConstants.EXISTING_ADDRESS1);
        culturalOffering.setBriefInfo(CulturalOfferingConstants.EXISTING_BRIEF_INFO1);
        culturalOffering.setLatitude(CulturalOfferingConstants.EXISTING_LATITUDE1);
        culturalOffering.setLongitude(CulturalOfferingConstants.EXISTING_LONGITUDE1);
        Subcategory subcategory = new Subcategory();
        subcategory.setId(CulturalOfferingConstants.EXISTING_SUBCATEGORY_ID1);
        culturalOffering.setSubcategory(subcategory);
        culturalOffering.setName(CulturalOfferingConstants.EXISTING_NAME1);
        culturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);

        return culturalOffering;
    }

    CulturalOfferingDto getExistingCulturalOfferingDto(){
        CulturalOfferingDto culturalOfferingDto= new CulturalOfferingDto();
        culturalOfferingDto.setAddress(CulturalOfferingConstants.EXISTING_ADDRESS1);
        culturalOfferingDto.setBriefInfo(CulturalOfferingConstants.EXISTING_BRIEF_INFO1);
        culturalOfferingDto.setLatitude(CulturalOfferingConstants.EXISTING_LATITUDE1);
        culturalOfferingDto.setLongitude(CulturalOfferingConstants.EXISTING_LONGITUDE1);
        culturalOfferingDto.setSubcategoryId(CulturalOfferingConstants.EXISTING_SUBCATEGORY_ID1);
        culturalOfferingDto.setName(CulturalOfferingConstants.EXISTING_NAME1);
        culturalOfferingDto.setId(CulturalOfferingConstants.EXISTING_ID1);

        return culturalOfferingDto;
    }

    @Test
    public void whenUpdate() {
        CulturalOfferingDto existingCulturalOffering = getExistingCulturalOfferingDto();
        existingCulturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);

        CulturalOfferingDto returnedCulturalOffering = culturalOfferingService.update(existingCulturalOffering);

        assertNotNull(returnedCulturalOffering);
        assertEquals(existingCulturalOffering.getId(), returnedCulturalOffering.getId());
        assertNotEquals(existingCulturalOffering.getName(), returnedCulturalOffering.getName());
    }

    @Test(expected = NullPointerException.class)
    public void whenUpdateNullPointerException(){
        culturalOfferingService.update(null);
    }



    @Test(expected = ResourceExistsException.class)
    public void whenCreateThrowEntityExists() {

        CulturalOfferingDto existing = getExistingCulturalOfferingDto();
        culturalOfferingService.create(existing);
    }

    @Test
    public void testCreate(){
        CulturalOfferingDto testCulturalOfferingDto = getTestCulturalOfferingDto();

        CulturalOfferingDto returnedCulturalOffering = culturalOfferingService.create(testCulturalOfferingDto);

        assertEquals(testCulturalOfferingDto.getId(), returnedCulturalOffering.getId());
        assertEquals(testCulturalOfferingDto.getName(), returnedCulturalOffering.getName());
        assertEquals(testCulturalOfferingDto.getAddress(), returnedCulturalOffering.getAddress());
    }

}
