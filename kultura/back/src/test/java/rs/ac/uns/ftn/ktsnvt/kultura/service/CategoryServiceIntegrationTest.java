package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.PAGE_SIZE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class CategoryServiceIntegrationTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Mapper mapper;

    @Test
    public void testReadAll() {
        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        Page<CategoryDto> returnedCategories = categoryService.readAll(pageRequest);

        assertEquals(CategoryConstants.DB_COUNT, returnedCategories.getContent().size());

    }

    @Test
    public void testReadById(){
        Optional<CategoryDto> cat = categoryService.readById(CategoryConstants.EXISTING_ID1);

        assertEquals(CategoryConstants.EXISTING_ID1, cat.get().getId());
        assertEquals(CategoryConstants.EXISTING_NAME1, cat.get().getName());

    }

    @Test
    @Transactional
    @Rollback(true)
    public void testCreate(){
        CategoryDto cat = new CategoryDto();
        cat.setId(CategoryConstants.TEST_CATEGORY_ID1);
        cat.setName(CategoryConstants.TEST_CATEGORY_NAME1);

        //max value jer uzimamo sve iz baze
        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        int dbSizeBeforeAdd = categoryService.readAll(pageRequest).getContent().size();

        CategoryDto dbCategory = categoryService.create(cat);

        assertThat(dbCategory).isNotNull();

        // Validate that new category is in the database
        List<CategoryDto> categories = categoryService.readAll(pageRequest).getContent();
        assertThat(categories).hasSize(dbSizeBeforeAdd + 1);
        dbCategory = categories.get(categories.size() - 3); //get the first category (?why not last?)
        assertThat(dbCategory.getId()).isEqualTo(CategoryConstants.TEST_CATEGORY_ID1);
        assertThat(dbCategory.getName()).isEqualTo(CategoryConstants.TEST_CATEGORY_NAME1);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testUpdate() {
        CategoryDto dbCategory = categoryService.readById(CategoryConstants.EXISTING_ID1).get();

        dbCategory.setName(CategoryConstants.TEST_CATEGORY_NAME1);

        dbCategory = categoryService.update(dbCategory);
        assertThat(dbCategory).isNotNull();

        //verify that database contains updated data
        dbCategory = categoryService.readById(CategoryConstants.EXISTING_ID1).get();
        assertThat(dbCategory.getName()).isEqualTo(CategoryConstants.TEST_CATEGORY_NAME1);
    }

}
