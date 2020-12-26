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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.PostConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.service.SubcategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SubcategoryControllerIntegrationTest {
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private TestRestTemplate restTemplate;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

    @Before
    public void login() {
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//        messageConverters.add(new FormHttpMessageConverter());
//        messageConverters.add(new StringHttpMessageConverter());
//        messageConverters.add(new MappingJackson2HttpMessageConverter());
//        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new LoginDto("admin@mail.com", "admin123"),
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
    public void whenGetSubcategoriesByCategoriyId(){
        Pageable pageRequest = PageRequest.of(0, 3);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<SubcategoryDto>> responseType =
                new ParameterizedTypeReference<HelperPage<SubcategoryDto>>() {};

        ResponseEntity<HelperPage<SubcategoryDto>> response = restTemplate.exchange(
                "/api/subcategories/category/" + CategoryConstants.EXISTING_ID1
                , HttpMethod.GET, httpEntity, responseType);

        List<SubcategoryDto> subcategories = response.getBody().getContent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subcategories.get(0).getCategoryId(), CategoryConstants.EXISTING_ID1);
    }

    @Test
    public void whenCreateSubcategory(){
        SubcategoryDto newSubcategory = createTestSubcategoryDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newSubcategory, headers);

        ResponseEntity<SubcategoryDto> response = restTemplate.exchange(
                "/api/subcategories", HttpMethod.POST, httpEntity, SubcategoryDto.class);

        SubcategoryDto createdSubcategory = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newSubcategory.getName(), createdSubcategory.getName());
    }

    @Test
    public void whenUpdateSubcategory(){
        SubcategoryDto newSubcategory = createTestSubcategoryDto();
        newSubcategory.setId(SubcategoryConstants.EXISTING_ID1);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newSubcategory, headers);

        ResponseEntity<SubcategoryDto> response = restTemplate.exchange(
                "/api/subcategories", HttpMethod.PUT, httpEntity, SubcategoryDto.class);

        SubcategoryDto updatedSubcategory = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SubcategoryConstants.TEST_NAME, updatedSubcategory.getName());
    }

    private SubcategoryDto createTestSubcategoryDto() {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setCategoryId(SubcategoryConstants.TEST_CATEGORY_ID);
        subcategoryDto.setName(SubcategoryConstants.TEST_NAME);

        return subcategoryDto;
    }

}


