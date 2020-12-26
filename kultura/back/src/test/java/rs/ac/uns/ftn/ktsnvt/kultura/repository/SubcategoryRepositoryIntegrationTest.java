package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.SubcategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class SubcategoryRepositoryIntegrationTest {
    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Test
    public void findAllByCategoryIdWhenExist(){
        Page<Subcategory> subcategoryPage = subcategoryRepository.findAllByCategoryId(SubcategoryConstants.TEST_CATEGORY_ID, Pageable.unpaged());

        assertFalse(subcategoryPage.isEmpty());

        assertFalse(subcategoryPage.get().anyMatch(subcategory ->
                subcategory.getCategory().getId()!=SubcategoryConstants.TEST_CATEGORY_ID));
    }

    @Test
    public void findAllByCategoryIdWhenNonExist(){
        long nonExistId = -5656L;
        Page<Subcategory> subcategoryPage = subcategoryRepository.findAllByCategoryId(nonExistId, Pageable.unpaged());

        assertTrue(subcategoryPage.isEmpty());
    }
}
