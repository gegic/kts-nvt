package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.hibernate.dialect.DB2Dialect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CulturalOfferingServiceIntegrationTest {

    @Autowired
    private CulturalOfferingService culturalOfferingService;
    @Autowired
    private CulturalOfferingRepository culturalOfferingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper modelMapper;


    @Test
    @Transactional
    public void testReadById(){
        Optional<CulturalOfferingDto> culturalOfferingDto = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1, -1);

        assertEquals(CulturalOfferingConstants.EXISTING_ID1, culturalOfferingDto.get().getId());
    }

    @Test
    @Transactional
    public void testReadAll() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        true,
                        -1,
                        -1,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                        );

        assertEquals(2, returnedCulturalOfferings.getContent().size());

    }

    @Test
    @Transactional
    public void testSearchByName() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "vasar",
                        1,
                        5,
                        true,
                        -1,
                        -1,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                );

        assertEquals(1, returnedCulturalOfferings.getContent().size());
        returnedCulturalOfferings.forEach(c -> {
            assertTrue(c.getName().toLowerCase().contains("vasar"));
        });
    }
    @Test
    @Transactional
    public void testOnlyWithReviews() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        false,
                        -1,
                        -1,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                );

        assertEquals(1, returnedCulturalOfferings.getContent().size());
        returnedCulturalOfferings.forEach(c -> {
            assertTrue(c.getNumReviews() > 0);
        });
    }

    @Test
    @Transactional
    public void testCategoryOne() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        true,
                        1,
                        -1,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                );

        returnedCulturalOfferings.forEach(c -> {
            assertEquals(1, c.getCategoryId());
        });
    }

    @Test
    @Transactional
    public void testSubcategoryTwo() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        true,
                        1,
                        2,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                );

        returnedCulturalOfferings.forEach(c -> {
            assertEquals(1, c.getCategoryId());
        });
    }

    @Test
    @Transactional
    public void testCategoryTwoNone() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        true,
                        2,
                        -1,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                );
        assertTrue(returnedCulturalOfferings.isEmpty());
    }

    @Test
    @Transactional
    public void testCoordinates() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        true,
                        -1,
                        -1,
                        40,
                        43,
                        20,
                        21,
                        -1
                );
        assertFalse(returnedCulturalOfferings.isEmpty());
        returnedCulturalOfferings.forEach(c -> {
            assertTrue(c.getLatitude() > 40 && c.getLatitude() < 43
            && c.getLongitude() > 20 && c.getLongitude() < 21);
        });
    }

    @Test
    @Transactional
    public void testCoordinatesEmpty() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        1,
                        5,
                        true,
                        -1,
                        -1,
                        20,
                        21,
                        20,
                        21,
                        -1
                );
        assertTrue(returnedCulturalOfferings.isEmpty());
    }

    @Test
    @Transactional
    public void testRating() {

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService
                .readAll(pageRequest,
                        "",
                        2.4f,
                        3.1f,
                        false,
                        -1,
                        -1,
                        -90,
                        90,
                        -180,
                        180,
                        -1
                );
        assertFalse(returnedCulturalOfferings.isEmpty());
        returnedCulturalOfferings.forEach(c -> {
            assertTrue(c.getOverallRating() > 2.4f &&
                    c.getOverallRating() < 3.1f);
        });
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        CulturalOfferingDto oldValues = culturalOfferingService.readById
                (CulturalOfferingConstants.EXISTING_ID1, -1).orElseThrow(() -> new Exception("Test invalid!"));

        CulturalOfferingDto dbCulturalOffering = culturalOfferingService.readById
                (CulturalOfferingConstants.EXISTING_ID1, -1).get();

        dbCulturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);

        dbCulturalOffering = culturalOfferingService.update(dbCulturalOffering);
        assertThat(dbCulturalOffering).isNotNull();

        //verify that database contains updated data
        dbCulturalOffering = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1, -1).get();
        assertThat(dbCulturalOffering.getName()).isEqualTo(CulturalOfferingConstants.TEST_NAME1);

        culturalOfferingService.update(oldValues);

    }

    @Test
    @Transactional
    public void testCreate(){
        CulturalOfferingDto culturalOfferingDto = new CulturalOfferingDto();
        culturalOfferingDto.setAddress(CulturalOfferingConstants.TEST_ADDRESS1);
        culturalOfferingDto.setBriefInfo(CulturalOfferingConstants.TEST_BRIEF_INFO1);
        culturalOfferingDto.setLatitude(CulturalOfferingConstants.TEST_LATITUDE1);
        culturalOfferingDto.setLongitude(CulturalOfferingConstants.TEST_LONGITUDE1);
        culturalOfferingDto.setSubcategoryId(CulturalOfferingConstants.TEST_SUBCATEGORY_ID1);
        culturalOfferingDto.setName(CulturalOfferingConstants.TEST_NAME1);
        culturalOfferingDto.setPhotoId(3L);

        CulturalOfferingDto created = culturalOfferingService.create(culturalOfferingDto);

        assertNotNull(created.getId());
        assertEquals(culturalOfferingDto.getName(), created.getName());
        assertEquals(culturalOfferingDto.getPhotoId(), created.getPhotoId());

        culturalOfferingRepository.deleteById(created.getId());
    }

//    @Test
//    @Transactional
//    public void testUpdate() {
//        CulturalOffering old = culturalOfferingRepository.findById(1L).get();
//        CulturalOfferingDto culturalOfferingDto = new CulturalOfferingDto();
//        culturalOfferingDto.setAddress(CulturalOfferingConstants.TEST_ADDRESS1);
//        culturalOfferingDto.setLatitude(CulturalOfferingConstants.TEST_LATITUDE1);
//        culturalOfferingDto.setName(CulturalOfferingConstants.TEST_NAME1);
//        culturalOfferingDto.setId(1L);
//
//        CulturalOfferingDto updated = culturalOfferingService.update(culturalOfferingDto);
//
//        assertEquals(old.getId(), updated.getId().longValue());
//        assertEquals(culturalOfferingDto.getAddress(), updated.getAddress());
//        assertEquals(culturalOfferingDto.getLatitude(), updated.getLatitude());
//        assertEquals(culturalOfferingDto.getName(), updated.getName());
//
//        culturalOfferingRepository.save(old);
//
//    } DZABE TE KUCAH

    @Test
    @Transactional
    public void testSubscribe() {
        CulturalOffering old = culturalOfferingRepository.findById(1L).get();
        long oldSubscribers = old.getSubscribedUsers() == null ? 0 : old.getSubscribedUsers().size();

        CulturalOfferingDto dto = culturalOfferingService.subscribe(1, 1);

        CulturalOffering newOffering = culturalOfferingRepository.findById(1L).get();
        assertEquals(oldSubscribers + 1, dto.getNumSubscribed().longValue());
        assertTrue(newOffering.getSubscribedUsers().stream().anyMatch(u -> u.getId() == 1L));

        newOffering.getSubscribedUsers().removeIf(u -> u.getId() == 1);
        culturalOfferingRepository.save(newOffering);
    }

    @Test
    @Transactional
    public void testUnsubscribe() {
        CulturalOffering old = culturalOfferingRepository.getOne(1L);
        User u = userRepository.getOne(1L);
        old.getSubscribedUsers().add(u);
        long oldSubscribers = old.getSubscribedUsers() == null ? 0 : old.getSubscribedUsers().size();
        culturalOfferingRepository.save(old);

        CulturalOfferingDto unsubscribed = culturalOfferingService.unsubscribe(1, 1);

        assertEquals(oldSubscribers - 1, unsubscribed.getNumSubscribed().longValue());

    }

}
