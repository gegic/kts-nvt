package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ForeignKeyException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SubCategoryServiceUnitTest {

    @MockBean
    private SubcategoryRepository subcategoryRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private Mapper mapper;

    @Autowired
    private SubcategoryService subcategoryService;


    private SubcategoryDto getNewSubCategoryDto() {
        SubcategoryDto subcategoryDto = new SubcategoryDto();
        subcategoryDto.setCategoryId(CategoryConstants.TEST_CATEGORY_ID1);
        subcategoryDto.setName(SubcategoryConstants.TEST_NAME);
        subcategoryDto.setCategoryName(CategoryConstants.TEST_CATEGORY_NAME1);
        return subcategoryDto;
    }

    private Subcategory getSubCategory() {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(SubcategoryConstants.EXISTING_ID1);
        subcategory.setName(SubcategoryConstants.EXISTING_NAME1);
        return subcategory;
    }

    CulturalOffering getExistingCulturalOffering(){
        CulturalOffering culturalOffering = new CulturalOffering();
        culturalOffering.setAddress(CulturalOfferingConstants.EXISTING_ADDRESS1);
        culturalOffering.setBriefInfo(CulturalOfferingConstants.EXISTING_BRIEF_INFO1);
        culturalOffering.setLatitude(CulturalOfferingConstants.EXISTING_LATITUDE1);
        culturalOffering.setLongitude(CulturalOfferingConstants.EXISTING_LONGITUDE1);
        culturalOffering.setName(CulturalOfferingConstants.EXISTING_NAME1);
        culturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);

        return culturalOffering;
    }

    @Test
    public void testCreate() {
        SubcategoryDto newDto = getNewSubCategoryDto();
        Mockito.when(mapper.fromDto(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            SubcategoryDto dto = i.getArgument(0);
            Subcategory sub = new Subcategory();
            sub.setName(dto.getName());

            Category c = new Category();
            c.setId(dto.getCategoryId());
            c.setName(dto.getCategoryName());

            sub.setCategory(c);
            return sub;
        });

        Mockito.when(subcategoryRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Mockito.when(categoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));
        Mockito.when(subcategoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            Subcategory sub = i.getArgument(0);
            SubcategoryDto dto = new SubcategoryDto();
            dto.setName(sub.getName());
            dto.setCategoryId(sub.getCategory().getId());
            dto.setCategoryName(sub.getCategory().getName());
            return dto;
        });



        SubcategoryDto resultDto = subcategoryService.create(newDto);

        assertNotNull(resultDto);
        assertEquals(SubcategoryConstants.TEST_NAME, resultDto.getName());
        assertEquals(CategoryConstants.TEST_CATEGORY_ID1, resultDto.getCategoryId());
        assertEquals(CategoryConstants.TEST_CATEGORY_NAME1, resultDto.getCategoryName());
    }

    @Test(expected = EntityExistsException.class)
    public void testCreateEntityExists() {
        SubcategoryDto newDto = getNewSubCategoryDto();
        Mockito.when(mapper.fromDto(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            SubcategoryDto dto = i.getArgument(0);
            Subcategory sub = new Subcategory();
            sub.setName(dto.getName());

            Category c = new Category();
            c.setId(sub.getId());
            c.setName(dto.getCategoryName());

            sub.setCategory(c);
            return sub;
        });

        Mockito.when(subcategoryRepository.existsById(Mockito.anyLong())).thenReturn(true);

        Mockito.when(categoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));
        Mockito.when(subcategoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            Subcategory sub = i.getArgument(0);
            SubcategoryDto dto = new SubcategoryDto();
            dto.setName(sub.getName());
            dto.setCategoryId(sub.getCategory().getId());
            dto.setCategoryName(sub.getCategory().getName());
            return dto;
        });



        subcategoryService.create(newDto);

    }



    @Test
    public void testUpdate() {
        SubcategoryDto updateDto = getNewSubCategoryDto();
        updateDto.setId(SubcategoryConstants.EXISTING_ID1);

        Mockito.when(mapper.fromDto(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            SubcategoryDto dto = i.getArgument(0);
            Subcategory sub = new Subcategory();
            sub.setName(dto.getName());
            return sub;
        });

        Mockito.when(subcategoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(getSubCategory()));

        Mockito.when(categoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));
        Mockito.when(subcategoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            Subcategory sub = i.getArgument(0);
            SubcategoryDto dto = new SubcategoryDto();
            dto.setName(sub.getName());
            return dto;
        });



        SubcategoryDto resultDto = subcategoryService.update(updateDto);

        assertNotNull(resultDto);
        assertEquals(SubcategoryConstants.TEST_NAME, resultDto.getName());
    }


    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateDoesntExist() {
        SubcategoryDto updateDto = getNewSubCategoryDto();
        updateDto.setId(SubcategoryConstants.EXISTING_ID1);

        Mockito.when(mapper.fromDto(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            SubcategoryDto dto = i.getArgument(0);
            Subcategory sub = new Subcategory();
            sub.setName(dto.getName());
            sub.setId(dto.getId());
            return sub;
        });

        Mockito.when(subcategoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Mockito.when(categoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));
        Mockito.when(subcategoryRepository.save(Mockito.any())).thenAnswer(i->i.getArgument(0));

        Mockito.when(mapper.fromEntity(Mockito.any(), Mockito.any())).thenAnswer(i -> {
            Subcategory sub = i.getArgument(0);
            SubcategoryDto dto = new SubcategoryDto();
            dto.setName(sub.getName());
            dto.setId(sub.getId());
            return dto;
        });



        subcategoryService.update(updateDto);

    }

    @Test
    public void testDeleteNoCulturalOfferings(){
        Long id = SubcategoryConstants.EXISTING_ID1;

        Mockito.when(subcategoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(getSubCategory()));

        Mockito.doNothing().when(subcategoryRepository).deleteById(Mockito.anyLong());

        subcategoryService.delete(id);

    }


    @Test(expected = ForeignKeyException.class)
    public void testDeleteHasCulturalOfferings(){
        Long id = SubcategoryConstants.EXISTING_ID1;


        Subcategory subcategory = getSubCategory();
        CulturalOffering culturalOffering = getExistingCulturalOffering();
        subcategory.addCulturalOffering(culturalOffering);
        culturalOffering.setId(CulturalOfferingConstants.EXISTING_ID1);
        Mockito.when(subcategoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(subcategory));

        Mockito.doNothing().when(subcategoryRepository).deleteById(Mockito.anyLong());

        subcategoryService.delete(id);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteDoesntExist(){
        Long id = SubcategoryConstants.EXISTING_ID1;

        Mockito.when(subcategoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Mockito.doNothing().when(subcategoryRepository).deleteById(Mockito.anyLong());

        subcategoryService.delete(id);

    }
}