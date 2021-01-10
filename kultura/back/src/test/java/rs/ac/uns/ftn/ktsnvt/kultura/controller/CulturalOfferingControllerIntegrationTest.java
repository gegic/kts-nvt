package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;


@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CulturalOfferingControllerIntegrationTest {
    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

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

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        // oldValues
        CulturalOfferingDto oldValues = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1).orElse(null);
        if (oldValues == null) {
            throw new Exception("Test invalid");
        }

        CulturalOfferingDto dbCulturalOffering = new CulturalOfferingDto();

        dbCulturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);
        dbCulturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);

        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(dbCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.PUT, httpEntity, CulturalOfferingDto.class);

        CulturalOfferingDto updatedCulturalOffering = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(dbCulturalOffering.getName()).isEqualTo(CulturalOfferingConstants.TEST_NAME1);

        this.accessToken = null;

        this.culturalOfferingService.update(oldValues);
    }

    CulturalOfferingDto getTestCulturalOfferingDto(){

        CulturalOfferingDto culturalOfferingDto= new CulturalOfferingDto();
        culturalOfferingDto.setAddress(CulturalOfferingConstants.TEST_ADDRESS1);
        culturalOfferingDto.setBriefInfo(CulturalOfferingConstants.TEST_BRIEF_INFO1);
        culturalOfferingDto.setLatitude(CulturalOfferingConstants.TEST_LATITUDE1);
        culturalOfferingDto.setLongitude(CulturalOfferingConstants.TEST_LONGITUDE1);
        culturalOfferingDto.setSubcategoryId(CulturalOfferingConstants.TEST_SUBCATEGORY_ID1);
        culturalOfferingDto.setName(CulturalOfferingConstants.TEST_NAME1);

        return culturalOfferingDto;
    }


}
