package rs.ac.uns.ftn.ktsnvt.kultura;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.TestPropertySource;
import rs.ac.uns.ftn.ktsnvt.kultura.controller.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOfferingMainPhoto;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.*;
import rs.ac.uns.ftn.ktsnvt.kultura.service.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // controllers
        AuthControllerIntegrationTest.class,
        CategoriesControllerIntegrationTest.class,
        CulturalOfferingControllerIntegrationTest.class,
        CulturalOfferingPhotosControllerIntegrationTest.class,
        PostControllerIntegrationTest.class,
        ReviewControllerIntegrationTest.class,
        SubcategoryControllerIntegrationTest.class,
        UsersControllerIntegrationTest.class,
        // services
        CategoryServiceIntegrationTest.class,
        CategoryServiceUnitTest.class,
        CulturalOfferingMainPhotoServiceIntegrationTest.class,
        CulturalOfferingPhotoServiceIntegrationTest.class,
        CulturalOfferingServiceIntegrationTest.class,
        CulturalOfferingServiceUnitTest.class,
        PostServiceIntegrationTest.class,
        PostServiceUnitTest.class,
        ReviewPhotoServiceIntegrationTest.class,
        ReviewServiceIntegrationTest.class,
        SubcategoryServiceIntegrationTest.class,
        UserServiceIntegrationTest.class,
        UserServiceUnitTest.class,
        // repositories
        AuthorityRepositoryIntegrationTest.class,
        CulturalOfferingMainPhotoRepositoryIntegrationTest.class,
        CulturalOfferingRepositoryIntegrationTest.class,
        PostRepositoryIntegrationTest.class,
        ReviewPhotoServiceIntegrationTest.class,
        ReviewRepositoryIntegrationTest.class,
        SubcategoryRepositoryIntegrationTest.class,
        UserRepositoryIntegrationTest.class
})
@TestPropertySource("classpath:test.properties")
public class SuiteAll {
}
