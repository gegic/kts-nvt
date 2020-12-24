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
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.StringDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.TokenResponse;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CategoryService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CategoriesControllerIntegrationTest {

    @Autowired
    private CategoryService categoryService;

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
    public void whenCreateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CategoryConstants.TEST_CATEGORY_NAME1);
        categoryDto.setId(CategoryConstants.TEST_CATEGORY_ID1);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(
                "/api/categories", HttpMethod.POST, httpEntity, CategoryDto.class);

        CategoryDto newCategory = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCategory.getId(), categoryDto.getId());
        assertEquals(newCategory.getName(), categoryDto.getName());
    }

    @Test
    public void whenUpdateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CategoryConstants.TEST_CATEGORY_NAME1);
        categoryDto.setId(CategoryConstants.EXISTING_ID1);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);

        ResponseEntity<CategoryDto> response = restTemplate.exchange(
                "/api/categories", HttpMethod.PUT, httpEntity, CategoryDto.class);

        CategoryDto updatedCategory = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCategory.getId(), CategoryConstants.EXISTING_ID1);
        assertEquals(updatedCategory.getName(), CategoryConstants.TEST_CATEGORY_NAME1);
    }



//    @Test
//    public void whenCreateCategoryCategoryExists(){
//        CategoryDto categoryDto = new CategoryDto();
//        categoryDto.setName(CategoryConstants.EXISTING_NAME1);
//        categoryDto.setId(CategoryConstants.EXISTING_ID1);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + this.accessToken);
//        HttpEntity<Object> httpEntity = new HttpEntity<>(categoryDto, headers);
//
//        ResponseEntity<EntityExistsException> response = restTemplate.exchange(
//                "/api/categories", HttpMethod.POST, httpEntity, EntityExistsException.class);
//
//        //assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("A category with this ID already exists", response.getBody().getMessage());
//    }

//    @Test
//    public void testGetAll() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + this.accessToken);
//        //kreiramo objekat koji saljemo u sklopu zahteva
//        // objekat nema telo, vec samo zaglavlje, jer je rec o GET zahtevu
//        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
//        //mora ovako jer ne moze se pozvati .class
//        ParameterizedTypeReference<HelperPage<CategoryDto>> responseType = new ParameterizedTypeReference<HelperPage<CategoryDto>>() {};
//        // posaljemo zahtev koji ima i zaglavlje u kojem je JWT token
//        ResponseEntity<HelperPage<CategoryDto>> responseEntity =
//                restTemplate.exchange("/api/categories?page=0&size=3", HttpMethod.GET, null, responseType);
//
////        List<CategoryDto> categories = responseEntity.getBody().getContent();
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
////        assertEquals(CategoryConstants.DB_COUNT, categories.size());
////        assertEquals(CategoryConstants.EXISTING_ID1, categories.get(0).getId());
////        assertEquals(CategoryConstants.EXISTING_ID2, categories.get(1).getId());
//    }




}
