package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ktsnvt.kultura.config.PhotosConfig;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewPhotoDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Review;
import rs.ac.uns.ftn.ktsnvt.kultura.model.ReviewPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewPhotoRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.ReviewRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.security.Token;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class ReviewPhotoServiceIntegrationTest {

    @Autowired
    ReviewPhotoService photoService;
    @Autowired
    ReviewPhotoRepository photoRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    Mapper mapper;
    @Autowired
    PhotosConfig photosConfig;


    @Test
    public void addPhotos() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile[] multipartFiles = new MultipartFile[2];
        multipartFiles[0] = new MockMultipartFile("photos", Files.readAllBytes(f.toPath()));
        multipartFiles[1] = new MockMultipartFile("photos", Files.readAllBytes(f.toPath()));

        List<ReviewPhotoDto> photoDtos = photoService.addPhotos(multipartFiles);

        photoDtos.forEach(p -> {
            assertTrue(new File(photosConfig.getPath() + "review/thumbnail/" + p.getId() + ".png").exists());
            assertTrue(new File(photosConfig.getPath() + "review/" + p.getId() + ".png").exists());
        });

        photoDtos.forEach(p -> {
            new File(photosConfig.getPath() + "review/thumbnail/" + p.getId() + ".png").delete();
            new File(photosConfig.getPath() + "review/" + p.getId() + ".png").delete();
        });
    }

    @Test
    public void clearPhotos() throws IOException {
        String authToken = "MOJMALITOKEN";
        User u = new User();
        Token authentication = new Token(u);
        authentication.setCredentials(authToken);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile[] multipartFiles = new MultipartFile[2];
        multipartFiles[0] = new MockMultipartFile("photos", Files.readAllBytes(f.toPath()));
        multipartFiles[1] = new MockMultipartFile("photos", Files.readAllBytes(f.toPath()));
        List<ReviewPhotoDto> dtos = photoService.addPhotos(multipartFiles);

        dtos.forEach(p -> {
            assertTrue(new File(photosConfig.getPath() + "review/thumbnail/" + p.getId() + ".png").exists());
            assertTrue(new File(photosConfig.getPath() + "review/" + p.getId() + ".png").exists());
        });

        photoService.clearPhotos();

        dtos.forEach(p -> {
            assertFalse(new File(photosConfig.getPath() + "review/thumbnail/" + p.getId() + ".png").exists());
            assertFalse(new File(photosConfig.getPath() + "review/" + p.getId() + ".png").exists());
        });

    }

    @Test
    @Transactional
    public void deleteForReview() throws IOException {
        File f = ResourceUtils.getFile("src/test/resources/test_photo.png");
        MultipartFile[] multipartFiles = new MultipartFile[2];
        multipartFiles[0] = new MockMultipartFile("photos", Files.readAllBytes(f.toPath()));
        multipartFiles[1] = new MockMultipartFile("photos", Files.readAllBytes(f.toPath()));
        List<ReviewPhotoDto> dtos = photoService.addPhotos(multipartFiles);
        Review r = reviewRepository.findById(1L).get();
        Set<ReviewPhoto> photos = photoRepository.findAll().stream().filter(p -> p.getReview() == null).collect(Collectors.toSet());
        r.setPhotos(photos);
        reviewRepository.save(r);

        photoService.deleteForReview(1L);

        assertTrue(photoRepository.findByReviewId(1L).isEmpty());
    }
}