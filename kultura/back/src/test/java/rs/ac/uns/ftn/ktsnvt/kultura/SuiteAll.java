package rs.ac.uns.ftn.ktsnvt.kultura;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.TestPropertySource;
import rs.ac.uns.ftn.ktsnvt.kultura.controller.*;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.*;
import rs.ac.uns.ftn.ktsnvt.kultura.service.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({AuthControllerIntegrationTest.class, CategoriesControllerIntegrationTest.class, CulturalOfferingControllerIntegrationTest.class,
        PostControllerIntegrationTest.class, ReviewControllerIntegrationTest.class, SubcategoryControllerIntegrationTest.class,
        UsersControllerIntegrationTest.class, CategoryServiceIntegrationTest.class, CategoryServiceUnitTest.class, CulturalOfferingServiceIntegrationTest.class,
        PostServiceUnitTest.class, PostServiceIntegrationTest.class, ReviewServiceIntegrationTest.class, SubcategoryServiceIntegrationTest.class,
        UserServiceIntegrationTest.class, UserServiceUnitTest.class, CategoryRepositoryIntegrationTest.class, CulturalOfferingRepositoryIntegrationTest.class,
        PostRepositoryIntegrationTest.class, ReviewRepositoryIntegrationTest.class, SubcategoryRepositoryIntegrationTest.class,
//        CulturalOfferingServiceUnitTest.class,
        UserRepositoryIntegrationTest.class})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {
}
