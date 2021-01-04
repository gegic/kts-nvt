package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CulturalOfferingServiceUnitTest {

    @Autowired
    private CulturalOfferingService culturalOfferingService;
    @MockBean
    private CulturalOfferingRepository culturalOfferingRepository;
    @MockBean
    private SubcategoryRepository subcategoryRepository;
    @MockBean
    private Mapper modelMapper;
    @Autowired
    private CulturalOfferingMainPhotoRepository photoRepository;

//    @Before
    public void setUp() {
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
        Category c = new Category();
        c.setId(1);
        c.setName("Cat");
        Subcategory subcategory = new Subcategory();
        subcategory.setCategory(c);
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

    @Test(expected = ResourceExistsException.class)
    public void whenCreateThrowEntityExists() {
        CulturalOfferingDto existingDto = getExistingCulturalOfferingDto();
        CulturalOffering existing = getExistingCulturalOffering();
        Mockito.when(modelMapper.fromDto(existingDto, CulturalOffering.class))
                .thenReturn(existing);

        Mockito.when(culturalOfferingRepository.existsById(CulturalOfferingConstants.EXISTING_ID1))
                .thenReturn(true);

        Mockito.when(culturalOfferingRepository.existsById(CulturalOfferingConstants.EXISTING_ID1))
                .thenReturn(true);

        culturalOfferingService.create(existingDto);
    }

    @Test
    public void testFindByBounds() {
        Mockito.when(culturalOfferingRepository.findByBounds(Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat())).thenAnswer(
                in -> {
                    float latitudeStart = in.getArgument(0);
                    float latitudeEnd = in.getArgument(1);
                    float longitudeStart = in.getArgument(2);
                    float longitudeEnd = in.getArgument(3);
                    CulturalOffering ex = getExistingCulturalOffering();
                    List<CulturalOffering> list = new ArrayList<>();
                    if (ex.getLatitude() > latitudeStart && ex.getLatitude() < latitudeEnd &&
                            ex.getLongitude() > longitudeStart && ex.getLongitude() < longitudeEnd) {
                        list.add(ex);
                    }
                    return list;
                }
        );

        Mockito.when(modelMapper.fromEntity(Mockito.any(CulturalOffering.class), Mockito.eq(CulturalOfferingDto.class)))
                .thenAnswer(i -> {
                    CulturalOffering c = i.getArgument(0);
                    return new CulturalOfferingDto(
                            c.getId(),
                            c.getName(),
                            c.getBriefInfo(),
                            c.getLatitude(),
                            c.getLongitude(),
                            c.getAddress(),
                            null,
                            c.getOverallRating(),
                            c.getNumReviews(),
                            c.getLastChange(),
                            c.getAdditionalInfo(),
                            c.getSubcategory().getId(),
                            c.getSubcategory().getName(),
                            c.getSubcategory().getCategory().getId(),
                            c.getSubcategory().getCategory().getName(),
                            0
                    );
                });
        List<CulturalOfferingDto> list = culturalOfferingService.findByBounds(9, 14, -4, 17);

        assertEquals(1, list.size());
        assertEquals(CulturalOfferingConstants.EXISTING_ID1, list.get(0).getId());
    }

    @Test
    public void testFindByBoundsEmpty() {
        Mockito.when(culturalOfferingRepository.findByBounds(Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat(), Mockito.anyFloat())).thenAnswer(
                in -> {
                    float latitudeStart = in.getArgument(0);
                    float latitudeEnd = in.getArgument(1);
                    float longitudeStart = in.getArgument(2);
                    float longitudeEnd = in.getArgument(3);
                    CulturalOffering ex = getExistingCulturalOffering();
                    List<CulturalOffering> list = new ArrayList<>();
                    if (ex.getLatitude() > latitudeStart && ex.getLatitude() < latitudeEnd &&
                            ex.getLongitude() > longitudeStart && ex.getLongitude() < longitudeEnd) {
                        list.add(ex);
                    }
                    return list;
                }
        );

        List<CulturalOfferingDto> list = culturalOfferingService.findByBounds(30, 46, 50, 60);

        assertTrue(list.isEmpty());
    }

}
