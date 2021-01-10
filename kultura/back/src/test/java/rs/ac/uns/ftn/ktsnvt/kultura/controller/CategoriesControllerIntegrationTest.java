package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.StringDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.TokenResponse;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CategoriesControllerIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;

    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

    @Test
    public void whenCreateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CategoryConstants.TEST_CATEGORY_NAME2);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(
                "/api/categories", HttpMethod.POST, httpEntity, CategoryDto.class);

        CategoryDto newCategory = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCategory.getName(), categoryDto.getName());

        this.accessToken = null;

        categoryService.delete(newCategory.getId());
    }

    @Test
    public void whenCreateReturnResourceExists(){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CategoryConstants.EXISTING_NAME1);
        categoryDto.setId(CategoryConstants.EXISTING_ID1);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(
                "/api/categories", HttpMethod.POST, httpEntity, CategoryDto.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        this.accessToken = null;
    }

    @Test
    public void whenUpdateReturnResourceNotFoundException(){
        CategoryDto cat = new CategoryDto();
        cat.setId(CategoryConstants.NON_EXISTING_ID);
        cat.setName(CategoryConstants.NON_EXISTING_NAME);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(cat, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(
                "/api/categories", HttpMethod.PUT, httpEntity, CategoryDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        this.accessToken = null;
    }

    @Test
    public void whenUpdateCategory() throws Exception {
        // oldValues
        CategoryDto oldValues = categoryService.readById(CategoryConstants.EXISTING_ID1).orElse(null);
        if (oldValues == null) {
            throw new Exception("Test invalid");
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CategoryConstants.TEST_CATEGORY_NAME1);
        categoryDto.setId(CategoryConstants.EXISTING_ID1);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(
                "/api/categories", HttpMethod.PUT, httpEntity, CategoryDto.class);

        CategoryDto updatedCategory = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCategory.getId(), CategoryConstants.EXISTING_ID1);
        assertEquals(updatedCategory.getName(), CategoryConstants.TEST_CATEGORY_NAME1);

        this.accessToken = null;

        this.categoryService.update(oldValues);
    }



    @Test
    public void whenCreateCategoryCategoryExists(){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CategoryConstants.EXISTING_NAME1);
        categoryDto.setId(CategoryConstants.EXISTING_ID1);

        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);

        ResponseEntity<ResourceExistsException> response = restTemplate.exchange(
                "/api/categories/", HttpMethod.POST, httpEntity, ResourceExistsException.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        this.accessToken = null;
    }

    @Test
    public void testGetAll() {
        this.accessToken = LoginUtil.login(restTemplate, ADMIN_EMAIL, ADMIN_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        //mora ovako jer ne moze se pozvati .class nad generickim ovim djavolom od Pagea, tugica
        ParameterizedTypeReference<HelperPage<CategoryDto>> responseType = new ParameterizedTypeReference<HelperPage<CategoryDto>>() {};
        // posaljemo zahtev koji ima i zaglavlje u kojem je JWT token
        ResponseEntity<HelperPage<CategoryDto>> responseEntity =
                restTemplate.exchange("/api/categories?page=0&size=3", HttpMethod.GET, httpEntity, responseType);

        List<CategoryDto> categories = responseEntity.getBody().getContent();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(CategoryConstants.DB_COUNT, categories.size());
        assertEquals(CategoryConstants.EXISTING_ID1, categories.get(0).getId());
        assertEquals(CategoryConstants.EXISTING_ID2, categories.get(1).getId());

        this.accessToken = null;
    }




}
