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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
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
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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

    @Test
    public void whenGetSubcategoriesByCategoriyId(){
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

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


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<SubcategoryDto> subcategories = response.getBody().getContent();
        assertEquals(subcategories.get(0).getCategoryId(), CategoryConstants.EXISTING_ID1);
        this.accessToken = null;
    }

    @Test
    public void whenGetSubcategoriesByCategoriyIdNonExisting(){
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<SubcategoryDto>> responseType =
                new ParameterizedTypeReference<HelperPage<SubcategoryDto>>() {};

        ResponseEntity<HelperPage<SubcategoryDto>> response = restTemplate.exchange(
                "/api/subcategories/category/" + CategoryConstants.NON_EXISTING_ID
                , HttpMethod.GET, httpEntity, responseType);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<SubcategoryDto> subcategories = response.getBody().getContent();
        assertEquals(0, subcategories.size());
        this.accessToken = null;
    }

    @Test
    public void whenCreateSubcategory(){
        SubcategoryDto newSubcategory = createTestSubcategoryDto();

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newSubcategory, headers);

        ResponseEntity<SubcategoryDto> response = restTemplate.exchange(
                "/api/subcategories", HttpMethod.POST, httpEntity, SubcategoryDto.class);

        SubcategoryDto createdSubcategory = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newSubcategory.getName(), createdSubcategory.getName());
        this.accessToken = null;

        this.subcategoryService.delete(createdSubcategory.getId());
    }



    @Test
    public void whenCreateSubcategoryNameExists(){
        SubcategoryDto newSubcategory = createExistingSubcategoryDto();

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newSubcategory, headers);

        ResponseEntity<SubcategoryDto> response = restTemplate.exchange(
                "/api/subcategories", HttpMethod.POST, httpEntity, SubcategoryDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        this.accessToken = null;

    }

    @Test
    public void whenUpdateSubcategory() throws Exception {
        SubcategoryDto oldValues = subcategoryService.findById(SubcategoryConstants.EXISTING_ID1).orElseThrow(() -> new Exception("Test invalid!"));

        SubcategoryDto newSubcategory = createTestSubcategoryDto();
        newSubcategory.setId(SubcategoryConstants.EXISTING_ID1);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(newSubcategory, headers);

        ResponseEntity<SubcategoryDto> response = restTemplate.exchange(
                "/api/subcategories", HttpMethod.PUT, httpEntity, SubcategoryDto.class);

        SubcategoryDto updatedSubcategory = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SubcategoryConstants.TEST_NAME, updatedSubcategory.getName());

        this.accessToken = null;

        subcategoryService.update(oldValues);
    }

    @Test
    public void whenUpdateSubcategoryNameExists() throws Exception {
        SubcategoryDto oldValues = subcategoryService.findById(SubcategoryConstants.EXISTING_ID1).orElseThrow(() -> new Exception("Test invalid!"));

        SubcategoryDto updateCategory = new SubcategoryDto();
        updateCategory.setId(SubcategoryConstants.EXISTING_ID1);
        updateCategory.setName(SubcategoryConstants.EXISTING_NAME2);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(updateCategory, headers);

        ResponseEntity<SubcategoryDto> response = restTemplate.exchange(
                "/api/subcategories", HttpMethod.PUT, httpEntity, SubcategoryDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        this.accessToken = null;

        subcategoryService.update(oldValues);
    }

    private SubcategoryDto createTestSubcategoryDto() {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setCategoryId(SubcategoryConstants.TEST_CATEGORY_ID);
        subcategoryDto.setName(SubcategoryConstants.TEST_NAME);

        return subcategoryDto;
    }
    private SubcategoryDto createExistingSubcategoryDto() {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setCategoryId(SubcategoryConstants.TEST_CATEGORY_ID);
        subcategoryDto.setName(SubcategoryConstants.EXISTING_NAME1);

        return subcategoryDto;
    }

}


