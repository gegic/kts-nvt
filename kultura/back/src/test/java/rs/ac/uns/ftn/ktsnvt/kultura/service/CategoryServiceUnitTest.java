package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.PAGE_SIZE;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.TEST_CATEGORY_ID1;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CategoryServiceUnitTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private Mapper mapper;

    @Before
    public void setupMapper() {
        Mockito.when(mapper.fromDto(Mockito.any(CategoryDto.class), Mockito.eq(Category.class))).thenAnswer(i -> {
            CategoryDto dto = i.getArgument(0);
            Category category = new Category();
            if (dto.getId() != null) {
                category.setId(dto.getId());
            }
            category.setName(dto.getName());
            return category;
        });

        Mockito.when(mapper.fromEntity(Mockito.any(Category.class), Mockito.eq(CategoryDto.class))).thenAnswer(i -> {
            Category category = i.getArgument(0);
            CategoryDto dto = new CategoryDto();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setNumSubcategories(category.getSubcategories() == null ? 0 : category.getSubcategories().size());
            return dto;
        });

        Mockito.when(mapper.toExistingEntity(Mockito.any(CategoryDto.class), Mockito.any(Category.class))).thenAnswer(i -> {
            Category category = i.getArgument(1);
            CategoryDto dto = i.getArgument(0);
            category.setName(dto.getName());
            return category;
        });
    }

    @Test
    public void testCreate() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Kategorija");
        Category converted = new Category();
        converted.setName("Kategorija");
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenAnswer(i -> {
            Category c = i.getArgument(0);
            c.setId(1);
            return c;
        });

        CategoryDto created = categoryService.create(categoryDto);

        assertNotNull(created.getId());
        assertEquals(categoryDto.getName(), created.getName());
    }

    @Test
    public void testUpdate() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Kategorija");
        Category found = new Category();
        found.setId(1);
        found.setName("Kategorija");
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(found));
        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenAnswer(i -> i.getArgument(0));


        CategoryDto updated = categoryService.update(categoryDto);

        assertEquals(found.getId(), updated.getId().longValue());
        assertEquals(categoryDto.getName(), updated.getName());
    }

    @Test(expected = NullPointerException.class)
    public void whenUpdateNullPointerException(){
        Mockito.when(categoryRepository.findById(null)).thenReturn(null);

        categoryService.update(null);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenUpdateEntityNotFoundException(){
        CategoryDto category = new CategoryDto();
        category.setId(555L);

        Mockito.when(categoryRepository.findById(555L)).thenReturn(Optional.empty());

        categoryService.update(category);
    }


    @Test
    public void testReadAll() {
        ArrayList<Category> categories = new ArrayList<>();
        Category cat1 = new Category();
        Category cat2 = new Category();
        categories.add(cat1);
        categories.add(cat2);

        Pageable pageRequest = PageRequest.of(0, PAGE_SIZE);

        Mockito.when(categoryRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(categories));

        Page<CategoryDto> returnedCategories = categoryService.readAll(pageRequest);

        assertEquals(categories.size(), returnedCategories.getContent().size());

    }

    @Test(expected = ResourceExistsException.class)
    public void whenCreateThrowEntityExists() {
            CategoryDto category = new CategoryDto();
            category.setId(CategoryConstants.TEST_CATEGORY_ID1);
            category.setName(CategoryConstants.TEST_CATEGORY_NAME1);

            CategoryDto category2 = new CategoryDto();
            category2.setId(CategoryConstants.TEST_CATEGORY_ID1);
            category2.setName(CategoryConstants.TEST_CATEGORY_NAME1);

            Mockito.when(categoryRepository.existsById(CategoryConstants.TEST_CATEGORY_ID1)).thenReturn(true);

            categoryService.create(category);
            categoryService.create(category2);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void whenDeleteEntityDoesntExist(){
        Mockito.when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.doNothing().when(categoryRepository).delete(Mockito.any());
        categoryService.delete(Mockito.anyLong());
    }
}
