package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CulturalOfferingServiceIntegrationTest {

    @Autowired
    private CulturalOfferingService culturalOfferingService;
    @Autowired
    private CulturalOfferingRepository culturalOfferingRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private Mapper modelMapper;


    @Test
    @Transactional
    public void testReadById(){
        Optional<CulturalOfferingDto> culturalOfferingDto = culturalOfferingService.readById(CulturalOfferingConstants.EXISTING_ID1);

        assertEquals(CulturalOfferingConstants.EXISTING_ID1, culturalOfferingDto.get().getId());

    }

    @Test
    @Transactional
    public void testReadAll() {
        Pageable pageRequest = PageRequest.of(0, CulturalOfferingConstants.PAGE_SIZE);

        Page<CulturalOfferingDto> returnedCulturalOfferings = culturalOfferingService.readAll(pageRequest);

        assertEquals(CulturalOfferingConstants.DB_COUNT, returnedCulturalOfferings.getContent().size());

    }

}
