package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingPhoto;

import java.util.List;

import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingMainPhotoConstants.TOKEN;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingMainPhotoRepositoryIntegrationTest {

    @Autowired
    CulturalOfferingMainPhotoRepository photoRepository;

    @Test
    public void testGetNullOffering() {
        CulturalOfferingMainPhoto photo = new CulturalOfferingMainPhoto();
        photo.setToken(TOKEN);
        photo.setWidth(100);
        photo.setHeight(100);
        photo.setCulturalOffering(null);
        photo = photoRepository.save(photo);


        List<CulturalOfferingMainPhoto> retrieved = photoRepository.getNullOffering(TOKEN);

        assertEquals(1, retrieved.size());
        assertNull(retrieved.get(0).getCulturalOffering());
        assertEquals(TOKEN, retrieved.get(0).getToken());

        photoRepository.deleteById(photo.getId());
    }
}