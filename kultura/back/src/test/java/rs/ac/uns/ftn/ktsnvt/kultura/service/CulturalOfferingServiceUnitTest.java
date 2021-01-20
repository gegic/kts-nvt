package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")

public class CulturalOfferingServiceUnitTest {

    @Autowired
    private CulturalOfferingService culturalOfferingService;
    @MockBean
    private CulturalOfferingRepository culturalOfferingRepository;
    @MockBean
    private SubcategoryRepository subcategoryRepository;
    @MockBean
    private Mapper modelMapper;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private CulturalOfferingMainPhotoRepository photoRepository;


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

    @Before
    public void setupMapper() {
        Mockito.when(modelMapper.fromDto(Mockito.any(CulturalOfferingDto.class), Mockito.eq(CulturalOffering.class))).thenAnswer(i -> {
            CulturalOfferingDto dto = i.getArgument(0);
            CulturalOffering offering = new CulturalOffering();
            if (dto.getId() != null) {
                offering.setId(dto.getId());
            }
            offering.setAddress(dto.getAddress());
            offering.setBriefInfo(dto.getBriefInfo());
            offering.setLatitude(dto.getLatitude());
            offering.setLongitude(dto.getLongitude());
            Category c = new Category();
            c.setId(dto.getCategoryId());
            c.setName(dto.getCategoryName());
            Subcategory subcategory = new Subcategory();
            subcategory.setCategory(c);
            subcategory.setName(dto.getSubcategoryName());
            subcategory.setId(dto.getSubcategoryId());
            offering.setSubcategory(subcategory);
            offering.setName(dto.getName());
            offering.setId(dto.getId());
            return offering;
        });

        Mockito.when(modelMapper.fromEntity(Mockito.any(CulturalOffering.class), Mockito.eq(CulturalOfferingDto.class))).thenAnswer(i -> {
            CulturalOffering culturalOffering = i.getArgument(0);
            CulturalOfferingDto dto = new CulturalOfferingDto();

            dto.setAddress(culturalOffering.getAddress());
            dto.setBriefInfo(culturalOffering.getBriefInfo());
            dto.setLatitude(culturalOffering.getLatitude());
            dto.setLongitude(culturalOffering.getLongitude());
            dto.setSubcategoryId(culturalOffering.getSubcategory().getId());
            dto.setName(culturalOffering.getName());
            dto.setId(culturalOffering.getId());

            dto.setNumSubscribed(culturalOffering.getSubscribedUsers().size());
            return dto;
        });

        Mockito.when(modelMapper.toExistingEntity(Mockito.any(CulturalOfferingDto.class), Mockito.any(CulturalOffering.class))).thenAnswer(i -> {
            CulturalOffering offering = i.getArgument(1);
            CulturalOfferingDto dto = i.getArgument(0);
            offering.setAddress(dto.getAddress());
            offering.setBriefInfo(dto.getBriefInfo());
            offering.setLatitude(dto.getLatitude());
            offering.setLongitude(dto.getLongitude());
            Category c = new Category();
            c.setId(dto.getCategoryId());
            c.setName(dto.getCategoryName());
            Subcategory subcategory = new Subcategory();
            subcategory.setCategory(c);
            subcategory.setName(dto.getSubcategoryName());
            subcategory.setId(dto.getSubcategoryId());
            offering.setSubcategory(subcategory);
            offering.setName(dto.getName());
            offering.setId(dto.getId());
            return offering;
        });
    }


    @Test(expected = ResourceExistsException.class)
    public void whenCreateThrowEntityExists() {
        CulturalOfferingDto existingDto = getExistingCulturalOfferingDto();

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

        List<CulturalOfferingDto> list = culturalOfferingService.findByBounds(9, 14, -4, 17, -1);

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

        List<CulturalOfferingDto> list = culturalOfferingService.findByBounds(30, 46, 50, 60, -1);

        assertTrue(list.isEmpty());
    }


    @Test
    public void whenUpdate(){
        CulturalOfferingDto newDto = getTestCulturalOfferingDto();
        newDto.setId(CulturalOfferingConstants.EXISTING_ID1);

        Mockito.when(culturalOfferingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(getExistingCulturalOffering()));
        Mockito.when(culturalOfferingRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        CulturalOfferingDto updatedDto = culturalOfferingService.update(newDto);

        assertEquals(updatedDto.getAddress(), newDto.getAddress());
        assertEquals(updatedDto.getName(), newDto.getName());
        assertEquals(updatedDto.getAdditionalInfo(), newDto.getAdditionalInfo());
        assertEquals(updatedDto.getBriefInfo(), newDto.getBriefInfo());
        assertEquals(updatedDto.getLongitude(), newDto.getLongitude());
        assertEquals(updatedDto.getLatitude(), newDto.getLatitude());
        assertEquals(updatedDto.getSubcategoryId(), newDto.getSubcategoryId());
    }

    @Test(expected = NullPointerException.class)
    public void whenUpdateNullException(){
        CulturalOfferingDto newDto = getTestCulturalOfferingDto();
        newDto.setId(null);

        Mockito.when(modelMapper.toExistingEntity(Mockito.any(), Mockito.any())).thenAnswer((i)-> i.<CulturalOffering>getArgument(1));

        Mockito.when(culturalOfferingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(culturalOfferingRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        culturalOfferingService.update(newDto);
    }

    @Test(expected = EntityNotFoundException.class)
    public void whenUpdateNotFoundException(){
        CulturalOfferingDto newDto = getTestCulturalOfferingDto();

        Mockito.when(modelMapper.toExistingEntity(Mockito.any(), Mockito.any())).thenAnswer((i)-> i.<CulturalOffering>getArgument(1));

        Mockito.when(culturalOfferingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(culturalOfferingRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        culturalOfferingService.update(newDto);
    }

    @Test
    public void testSubscribe() {
        CulturalOffering culturalOffering = getExistingCulturalOffering();
        Mockito.when(culturalOfferingRepository.findById(CulturalOfferingConstants.EXISTING_ID1)).thenReturn(Optional.ofNullable(culturalOffering));

        User u = new User();
        u.setId(1);
        u.setEmail("mail@mail.mail");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Mockito.when(culturalOfferingRepository.save(Mockito.any(CulturalOffering.class))).thenAnswer(i -> i.getArgument(0));

        CulturalOfferingDto dto = culturalOfferingService.subscribe(1, 1);

        assertEquals(CulturalOfferingConstants.EXISTING_ID1, dto.getId());
        assertTrue(dto.getSubscribed());
        assertEquals(1, dto.getNumSubscribed().longValue());
    }

    @Test
    public void testUnsubscribe() {
        CulturalOffering culturalOffering = getExistingCulturalOffering();

        User u = new User();
        u.setId(1);
        u.setEmail("mail@mail.mail");
        culturalOffering.getSubscribedUsers().add(u);
        Mockito.when(culturalOfferingRepository.findById(CulturalOfferingConstants.EXISTING_ID1)).thenReturn(Optional.ofNullable(culturalOffering));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(u));

        Mockito.when(culturalOfferingRepository.save(Mockito.any(CulturalOffering.class))).thenAnswer(i -> i.getArgument(0));

        CulturalOfferingDto dto = culturalOfferingService.unsubscribe(1, 1);

        assertEquals(CulturalOfferingConstants.EXISTING_ID1, dto.getId());
        assertEquals(0, dto.getNumSubscribed().longValue());
    }
}
