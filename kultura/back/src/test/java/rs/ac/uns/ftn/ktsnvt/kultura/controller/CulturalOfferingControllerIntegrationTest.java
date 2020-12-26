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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingControllerIntegrationTest {
    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

    // pre izvrsavanja testa, prijava da bismo dobili token
    @Before
    public void login() {
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//        messageConverters.add(new FormHttpMessageConverter());
//        messageConverters.add(new StringHttpMessageConverter());
//        messageConverters.add(new MappingJackson2HttpMessageConverter());
//        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new LoginDto("moderator@mail.com", "admin123"),
                        String.class);
        JsonNode parent= null;
        try {
            parent = new ObjectMapper().readTree(responseEntity.getBody());
            accessToken = parent.path("token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //TokenResponse tr = mapper.fromEntity(responseEntity.getBody(), TokenResponse.class);

    }

    @Test
    public void whenCreateCulturalOffering() {
        CulturalOfferingDto newCulturalOffering = getTestCulturalOfferingDto();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.POST, httpEntity, CulturalOfferingDto.class);

        CulturalOfferingDto createdCulturalOffering = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCulturalOffering.getName(), newCulturalOffering.getName());
    }

    @Test
    public void testUpdate(){
        CulturalOfferingDto dbCulturalOffering = new CulturalOfferingDto();

        dbCulturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);
        dbCulturalOffering.setName(CulturalOfferingConstants.TEST_NAME1);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(dbCulturalOffering, headers);

        ResponseEntity<CulturalOfferingDto> response = restTemplate.exchange(
                "/api/cultural-offerings", HttpMethod.PUT, httpEntity, CulturalOfferingDto.class);

        CulturalOfferingDto updatedCulturalOffering = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(dbCulturalOffering.getName()).isEqualTo(CulturalOfferingConstants.TEST_NAME1);

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
