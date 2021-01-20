package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;


import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;


@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingControllerIntegrationTest {
    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @Autowired
    private CulturalOfferingRepository culturalOfferingRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;


    @Test
    public void testFindById(){
        Long existingId1 = CulturalOfferingConstants.EXISTING_ID1;

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings/"+existingId1, HttpMethod.GET, httpEntity, CulturalOfferingDto.class);

        CulturalOfferingDto culturalOfferingDto = response.getBody();
        assertNotNull(culturalOfferingDto);
        assertEquals(culturalOfferingDto.getId(), existingId1);

    }

    @Test
    public void testFindByIdNotFound(){
        Long nonExistingId = CulturalOfferingConstants.TEST_ID1;

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings/"+nonExistingId, HttpMethod.GET, httpEntity, CulturalOfferingDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void whenCreateCulturalOffering() {
        CulturalOfferingDto newCulturalOffering = getTestCulturalOfferingDto();

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.POST, httpEntity, CulturalOfferingDto.class);

        CulturalOfferingDto createdCulturalOffering = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCulturalOffering.getName(), newCulturalOffering.getName());

        this.accessToken = null;
        culturalOfferingService.delete(createdCulturalOffering.getId());
    }

    @Test()
    public void whenCreate_ResourceExistsException() throws ResourceExistsException {
        CulturalOfferingDto newCulturalOffering = getTestCulturalOfferingDto();
        newCulturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.POST, httpEntity, CulturalOfferingDto.class);

    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        // oldValues
        CulturalOfferingDto oldValues = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1, -1).orElse(null);
        if (oldValues == null) {
            throw new Exception("Test invalid");
        }

        CulturalOfferingDto dbCulturalOffering = getTestCulturalOfferingDto();
        dbCulturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(dbCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.PUT, httpEntity, CulturalOfferingDto.class);

        CulturalOfferingDto updatedCulturalOffering = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(updatedCulturalOffering.getName()).isEqualTo(CulturalOfferingConstants.TEST_NAME1);
        assertEquals(updatedCulturalOffering.getBriefInfo(),CulturalOfferingConstants.TEST_BRIEF_INFO1);
        assertEquals(updatedCulturalOffering.getLatitude(),CulturalOfferingConstants.TEST_LATITUDE1);
        assertEquals(updatedCulturalOffering.getLongitude(),CulturalOfferingConstants.TEST_LONGITUDE1);
        assertEquals(updatedCulturalOffering.getAddress(),CulturalOfferingConstants.TEST_ADDRESS1);

        this.accessToken = null;

        this.culturalOfferingService.update(oldValues);
    }

    @Test
    public void testUpdateDoesntExist() throws EntityNotFoundException {

        CulturalOfferingDto dbCulturalOffering = new CulturalOfferingDto();

        dbCulturalOffering.setId(CulturalOfferingConstants.TEST_ID1);
        dbCulturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(dbCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.PUT, httpEntity, CulturalOfferingDto.class);

//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//
//        this.accessToken = null;
    }

//    @Test
//    @Transactional
//    public void testDelete() throws Exception {
//
//        CulturalOfferingDto old = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1).orElse(null);
//        if(old == null){
//            throw new Exception("Invalid test");
//        }
//
//        Long id = CulturalOfferingConstants.EXISTING_ID1;
//
//        Pageable p = PageRequest.of(0, 5);
//        Page<CulturalOfferingDto> all = culturalOfferingService.readAll(p);
//        long sizeBefore = all.getTotalElements();
//
//
//        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + this.accessToken);
//        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
//
//
//        ResponseEntity<CulturalOfferingDto> response = doDelete(id, httpEntity);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        p = PageRequest.of(0, 5);
//        all = culturalOfferingService.readAll(p);
//        long sizeAfter = all.getTotalElements();
//
//        assertEquals(sizeAfter+1, sizeBefore);
//
//        all.forEach(c->assertNotEquals(id, c.getId()));
//
//        culturalOfferingService.create(old);
//        old = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1).orElse(null);
//        if(old == null){
//            throw new Exception("Invalid test");
//        }
//    }
//
//    @Transactional
//    public ResponseEntity<CulturalOfferingDto> doDelete(Long id, HttpEntity<?> httpEntity) {
//        return restTemplate.exchange(
//                "/api/cultural-offerings/id/"+id, HttpMethod.DELETE, httpEntity, CulturalOfferingDto.class);
//    }

    CulturalOfferingDto getTestCulturalOfferingDto(){

        CulturalOfferingDto culturalOfferingDto= new CulturalOfferingDto();
        culturalOfferingDto.setAddress(CulturalOfferingConstants.TEST_ADDRESS1);
        culturalOfferingDto.setBriefInfo(CulturalOfferingConstants.TEST_BRIEF_INFO1);
        culturalOfferingDto.setLatitude(CulturalOfferingConstants.TEST_LATITUDE1);
        culturalOfferingDto.setLongitude(CulturalOfferingConstants.TEST_LONGITUDE1);
        culturalOfferingDto.setSubcategoryId(CulturalOfferingConstants.TEST_SUBCATEGORY_ID1);
        culturalOfferingDto.setName(CulturalOfferingConstants.TEST_NAME1);
        culturalOfferingDto.setPhotoId(3L);

        return culturalOfferingDto;
    }



}
