package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.ReviewConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.ReviewDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.PAGE_SIZE;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SubcategoryServiceIntegrationTest {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private SubcategoryService subcategoryService;


    @Test
    @Transactional
    public void testFindAllByCategoryId(){

        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        Page<SubcategoryDto> returnedSubcategories = subcategoryService
                .findAllByCategoryId(SubcategoryConstants.EXISTING_CATEGORY_ID1, pageRequest);

        assertEquals(SubcategoryConstants.DB_COUNT, returnedSubcategories.getContent().size());
    }

    @Test
    @Transactional
    public void testFindById(){
        Optional<SubcategoryDto> subcategory = subcategoryService.findById(SubcategoryConstants.EXISTING_ID1);

        assertEquals(SubcategoryConstants.EXISTING_CATEGORY_ID1, (Long)subcategory.get().getCategoryId());
        assertEquals(SubcategoryConstants.EXISTING_ID1, (Long)subcategory.get().getId());
        assertEquals(SubcategoryConstants.EXISTING_NAME1, subcategory.get().getName());

    }

    @Test
    @Transactional
    //@Rollback(true)
    public void testCreate(){
        SubcategoryDto newSubcategory = createTestSubcategoryDto();

        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        SubcategoryDto createdSubcategory = subcategoryService.create(newSubcategory);

        assertThat(createdSubcategory).isNotNull();

        // Validate that new category is in the database
        List<SubcategoryDto> subcategories = subcategoryService
                .findAllByCategoryId(SubcategoryConstants.EXISTING_CATEGORY_ID1,pageRequest).getContent();
        assertThat(subcategories).hasSize(SubcategoryConstants.DB_COUNT + 1);
        assertEquals(createdSubcategory.getCategoryId(), SubcategoryConstants.TEST_CATEGORY_ID);
        assertEquals(createdSubcategory.getName(), SubcategoryConstants.TEST_NAME);

        subcategoryService.delete(createdSubcategory.getId());
    }

    @Test
    @Transactional
    //@Rollback(true)
    public void testUpdate() {
        SubcategoryDto oldValues = subcategoryService.findById(SubcategoryConstants.EXISTING_ID1).get();


        SubcategoryDto dbSubcategory = subcategoryService.findById(SubcategoryConstants.EXISTING_ID1).get();

        dbSubcategory.setName(SubcategoryConstants.TEST_NAME);

        SubcategoryDto dbSubcategoryDto = new SubcategoryDto();
        dbSubcategoryDto.setId(dbSubcategory.getId());
        dbSubcategoryDto.setName(dbSubcategory.getName());
        dbSubcategoryDto.setCategoryId(dbSubcategory.getCategoryId());

        SubcategoryDto returnedSubcategory;

        returnedSubcategory = subcategoryService.update(dbSubcategoryDto);
        assertThat(returnedSubcategory).isNotNull();

        //verify that database contains updated data
        assertThat(returnedSubcategory.getName()).isEqualTo(SubcategoryConstants.TEST_NAME);

        subcategoryService.update(oldValues);
    }

    private SubcategoryDto createTestSubcategoryDto() {
        SubcategoryDto subcategoryDto = new SubcategoryDto();

        subcategoryDto.setCategoryId(SubcategoryConstants.TEST_CATEGORY_ID);
        subcategoryDto.setName(SubcategoryConstants.TEST_NAME);

        return subcategoryDto;
    }


}
