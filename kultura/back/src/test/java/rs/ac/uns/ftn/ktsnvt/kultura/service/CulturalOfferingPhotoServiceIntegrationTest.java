package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingPhotoServiceIntegrationTest {

    @Autowired
    CulturalOfferingPhotoService photoService;

    @Autowired
    CulturalOfferingPhotoRepository photoRepository;
    @Autowired
    CulturalOfferingRepository culturalOfferingRepository;
    @Autowired
    Mapper mapper;
    @Autowired
    PhotosConfig photosConfig;

    @Test
    public void readAllByCulturalOfferingId() {
        Pageable p = PageRequest.of(0, Integer.MAX_VALUE);
        Page<CulturalOfferingPhotoDto> photos = photoService.readAllByCulturalOfferingId(1, p);

        assertEquals(1, photos.getTotalElements());
    }

    @Test
    @Transactional
    public void create() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));

        CulturalOfferingPhotoDto photo = photoService.create(multipartFile, 1);

        File thumbnailFile = new File(photosConfig.getPath() + "thumbnail/" + photo.getId() + ".png");
        File photoFile = new File(photosConfig.getPath() + photo.getId() + ".png");

        assertNotNull(photo.getId());
        assertTrue(thumbnailFile.exists());
        assertTrue(photoFile.exists());

        photoService.delete(photo.getId());
    }

    @Test
    @Transactional
    public void delete() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        CulturalOfferingPhotoDto p = photoService.create(multipartFile, 2);

        assertTrue(new File(photosConfig.getPath() + "thumbnail/" + p.getId() + ".png").exists());
        assertTrue(new File(photosConfig.getPath() + p.getId() + ".png").exists());

        photoService.delete(p.getId());

        assertFalse(new File(photosConfig.getPath() + "thumbnail/" + p.getId() + ".png").exists());
        assertFalse(new File(photosConfig.getPath() + p.getId() + ".png").exists());
    }

    @Test
    @Transactional
    public void deleteByCulturalOffering() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile multipartFile = new MockMultipartFile("photo", Files.readAllBytes(f.toPath()));
        CulturalOfferingPhotoDto p1 = photoService.create(multipartFile, 2);
        CulturalOfferingPhotoDto p2 = photoService.create(multipartFile, 2);

        assertTrue(new File(photosConfig.getPath() + "thumbnail/" + p1.getId() + ".png").exists());
        assertTrue(new File(photosConfig.getPath() + p1.getId() + ".png").exists());
        assertTrue(new File(photosConfig.getPath() + "thumbnail/" + p2.getId() + ".png").exists());
        assertTrue(new File(photosConfig.getPath() + p2.getId() + ".png").exists());

        photoService.deleteByCulturalOffering(2);

        assertFalse(new File(photosConfig.getPath() + "thumbnail/" + p1.getId() + ".png").exists());
        assertFalse(new File(photosConfig.getPath() + p1.getId() + ".png").exists());
        assertFalse(new File(photosConfig.getPath() + "thumbnail/" + p2.getId() + ".png").exists());
        assertFalse(new File(photosConfig.getPath() + p2.getId() + ".png").exists());

    }
}