package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.SubcategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.SubcategoryRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants.PAGE_SIZE;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class SubcategoryServiceIntegrationTest {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager em;


    @Test
    @Transactional
    public void testFindAllByCategoryId(){

        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<SubcategoryDto> returnedSubcategories = subcategoryService
                .findAllByCategoryId(1L, pageRequest);

        long dbSize = subcategoryRepository.findAll().stream().filter(s -> s.getCategory().getId() == 1L).count();

        assertEquals(dbSize, returnedSubcategories.getTotalElements());
    }

    @Test
    @Transactional
    public void testFindById(){
        Optional<SubcategoryDto> subcategory = subcategoryService.findById(1L);

        assertEquals(1L, subcategory.get().getId().longValue());
    }

    @Test
    @Transactional
    //@Rollback(true)
    public void testCreate(){
        SubcategoryDto newSubcategory = new SubcategoryDto();
        newSubcategory.setCategoryId(1L);
        newSubcategory.setName("PotkategorijaNova");
        long oldDb = subcategoryRepository.count();


        SubcategoryDto createdSubcategory = subcategoryService.create(newSubcategory);

        long newDb = subcategoryRepository.count();
        assertNotNull(createdSubcategory);
        assertEquals(oldDb + 1, newDb);
        assertEquals(newSubcategory.getName(), createdSubcategory.getName());
        Subcategory s = subcategoryRepository.findById(createdSubcategory.getId()).get();
        s.setCategory(null);
        subcategoryRepository.delete(s);
    }

    @Test
    @Transactional
    public void testUpdate() {

        Subcategory old = subcategoryRepository.findById(1L).get();
        em.detach(old);

        SubcategoryDto dbSubcategoryDto = new SubcategoryDto();
        dbSubcategoryDto.setId(old.getId());
        dbSubcategoryDto.setName("PROMIJENJENO IME");
        dbSubcategoryDto.setCategoryId(old.getCategory().getId());

        SubcategoryDto returnedSubcategory = subcategoryService.update(dbSubcategoryDto);

        assertNotNull(returnedSubcategory);

        assertEquals(dbSubcategoryDto.getName(), returnedSubcategory.getName());

        subcategoryRepository.save(old);
    }

    @Test
    @Transactional
    public void testDelete() {
        Subcategory newSubcategory = new Subcategory();
        Category c = categoryRepository.findById(1L).get();
        c.getSubcategories().add(newSubcategory);
        newSubcategory.setCategory(c);
        newSubcategory.setName("PotkategorijaNova");
        newSubcategory = subcategoryRepository.save(newSubcategory);

        long oldDb = subcategoryRepository.count();

        subcategoryService.delete(newSubcategory.getId());

        long newDb = subcategoryRepository.count();

        assertEquals(oldDb - 1, newDb);
    }

}
