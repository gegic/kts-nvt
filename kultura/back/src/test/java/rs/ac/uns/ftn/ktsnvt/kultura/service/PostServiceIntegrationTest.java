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
import rs.ac.uns.ftn.ktsnvt.kultura.constants.CulturalOfferingConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.PostConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CategoryDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private Mapper mapper;

    private PostDto createTestPostDto() {
        PostDto postDto = new PostDto();

        postDto.setContent(PostConstants.TEST_CONTENT);
        postDto.setCulturalOfferingId(PostConstants.TEST_CULTURAL_OFFERING_ID);

        return postDto;
    }

    @Test
    @Transactional
    public void testReadAllByCulturalOfferingId(){
        Pageable pageRequest = PageRequest.of(0, PostConstants.PAGE_SIZE);

        Page<PostDto> returnedPosts = postService
                .readAllByCulturalOfferingId(CulturalOfferingConstants.EXISTING_ID1, pageRequest);

        assertEquals(CategoryConstants.DB_COUNT, returnedPosts.getContent().size());
    }

    @Test
    @Transactional
    public void testReadById(){
        Optional<PostDto> returnedPost = postService.readById(PostConstants.EXISTING_ID1);

        assertEquals(PostConstants.EXISTING_ID1, returnedPost.get().getId());
        assertEquals(PostConstants.EXISTING_CONTENT1, returnedPost.get().getContent());
    }

    @Test
    @Transactional
    public void testSave(){
        PostDto newPost = createTestPostDto();
        PostDto createdPost = postService.save(newPost);

        assertThat(createdPost).isNotNull();

        // Validate that new category is in the database
        Pageable pageRequest = PageRequest.of(0, PostConstants.PAGE_SIZE);
        List<PostDto> posts = postService
                .readAllByCulturalOfferingId(CulturalOfferingConstants.EXISTING_ID1, pageRequest).getContent();
        assertThat(posts).hasSize(PostConstants.DB_COUNT + 1);
        assertThat(PostConstants.TEST_ID).isEqualTo(createdPost.getId());
        assertThat(createdPost.getContent()).isEqualTo(newPost.getContent());

        postService.delete(createdPost.getId());
    }


}
