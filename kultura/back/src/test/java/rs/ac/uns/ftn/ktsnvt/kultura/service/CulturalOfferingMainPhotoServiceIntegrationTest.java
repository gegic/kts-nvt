package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingMainPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.security.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingMainPhotoServiceIntegrationTest {

    @Autowired
    CulturalOfferingMainPhotoService photoService;
    @Autowired
    CulturalOfferingMainPhotoRepository repository;
    @Autowired
    Mapper mapper;
    @Autowired
    PhotosConfig photosConfig;

    @Test
    public void addPhoto() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        CulturalOfferingPhotoDto photo = photoService.addPhoto(multipartFile);
        File thumbnailFile = new File(photosConfig.getPath() + "main/thumbnail/" + photo.getId() + ".png");
        File photoFile = new File(photosConfig.getPath() + "main/" + photo.getId() + ".png");

        assertNotNull(photo.getId());
        assertTrue(thumbnailFile.exists());
        assertTrue(photoFile.exists());

        photoFile.delete();
        thumbnailFile.delete();
        repository.deleteById(photo.getId());
    }

    @Test
    public void clearPhotos() throws IOException {
        String authToken = "MOJMALITOKEN";
        User u = new User();
        Token authentication = new Token(u);
        authentication.setCredentials(authToken);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<CulturalOfferingPhotoDto> photos = new ArrayList<>();

        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        photos.add(photoService.addPhoto(multipartFile));
        photos.add(photoService.addPhoto(multipartFile));
        photos.add(photoService.addPhoto(multipartFile));
        photos.add(photoService.addPhoto(multipartFile));
        photos.forEach(p -> {
            assertTrue(new File(photosConfig.getPath() + "main/thumbnail/" + p.getId() + ".png").exists());
            assertTrue(new File(photosConfig.getPath() + "main/" + p.getId() + ".png").exists());
        });
        photoService.clearPhotos();

        photos.forEach(p -> {
            assertFalse(new File(photosConfig.getPath() + "main/thumbnail/" + p.getId() + ".png").exists());
            assertFalse(new File(photosConfig.getPath() + "main/" + p.getId() + ".png").exists());
        });
    }

    @Test
    public void deletePhoto() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        CulturalOfferingPhotoDto photo = photoService.addPhoto(multipartFile);
        CulturalOfferingMainPhoto p = repository.getOne(photo.getId());

        assertTrue(new File(photosConfig.getPath() + "main/thumbnail/" + p.getId() + ".png").exists());
        assertTrue(new File(photosConfig.getPath() + "main/" + p.getId() + ".png").exists());

        photoService.deletePhoto(p);

        assertFalse(new File(photosConfig.getPath() + "main/thumbnail/" + p.getId() + ".png").exists());
        assertFalse(new File(photosConfig.getPath() + "main/" + p.getId() + ".png").exists());
    }
}