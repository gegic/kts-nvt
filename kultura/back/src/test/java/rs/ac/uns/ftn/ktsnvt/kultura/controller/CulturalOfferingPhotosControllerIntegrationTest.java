package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.service.CulturalOfferingPhotoService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.LoginUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.UserConstants.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingPhotosControllerIntegrationTest {

    @Autowired
    private CulturalOfferingPhotoService photoService;

    @Autowired
    private CulturalOfferingPhotoRepository photoRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    Mapper mapper;

    @Autowired
    PhotosConfig photosConfig;


    // JWT token za pristup REST servisima. Bice dobijen pri logovanju
    private String accessToken;

    @Test
    public void get() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        CulturalOfferingPhotoDto photo = photoService.create(multipartFile, 1L);

        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity
                = new HttpEntity<>(headers);

        ParameterizedTypeReference<HelperPage<CulturalOfferingPhotoDto>> responseType =
                new ParameterizedTypeReference<HelperPage<CulturalOfferingPhotoDto>>() {};

        ResponseEntity<HelperPage<CulturalOfferingPhotoDto>> response = restTemplate.exchange(
                "/api/photos/cultural-offering/1", HttpMethod.GET, httpEntity, responseType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        HelperPage<CulturalOfferingPhotoDto> body = response.getBody();

        assertNotNull(body);
        assertEquals(2, body.getTotalElements());

        photoService.delete(photo.getId());
    }

    @Test
    public void add() throws IOException {
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);
        ClassPathResource r = new ClassPathResource("test_photo.png");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("photo", r);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> httpEntity
                = new HttpEntity<>(body, headers);

        ResponseEntity<CulturalOfferingPhotoDto> response = restTemplate.exchange(
                "/api/photos/cultural-offering/1", HttpMethod.POST, httpEntity, CulturalOfferingPhotoDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CulturalOfferingPhotoDto photoDto = response.getBody();
        assertNotNull(photoDto);
        assertEquals(1, photoDto.getCulturalOfferingId().longValue());
        File thumbnailFile = new File(photosConfig.getPath() + "thumbnail/" + photoDto.getId() + ".png");
        File photoFile = new File(photosConfig.getPath() + photoDto.getId() + ".png");

        assertNotNull(photoDto.getId());
        assertTrue(thumbnailFile.exists());
        assertTrue(photoFile.exists());

        photoService.delete(photoDto.getId());
    }

    @Test
    public void delete() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        CulturalOfferingPhotoDto photo = photoService.create(multipartFile, 1L);

        long oldDb = photoRepository.count();
        this.accessToken = LoginUtil.login(restTemplate, MODERATOR_EMAIL, MODERATOR_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.accessToken);
        HttpEntity<Object> httpEntity
                = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/photos/" + photo.getId(), HttpMethod.DELETE, httpEntity, Void.class);

        long newDb = photoRepository.count();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(oldDb - 1, newDb);
    }
}