package rs.ac.uns.ftn.ktsnvt.kultura.repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CategoryConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryIntegrationTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void findByIdWhenExist(){
        Optional<Category> category  = categoryRepository.findById(CategoryConstants.EXISTING_ID1);
        assertTrue(category.isPresent());
    }

    @Test
    public void findByIdWhenNotExist(){
        long nonExistId = -5656L;
        Optional<Category> category  = categoryRepository.findById(nonExistId);
        assertFalse(category.isPresent());
    }
}
