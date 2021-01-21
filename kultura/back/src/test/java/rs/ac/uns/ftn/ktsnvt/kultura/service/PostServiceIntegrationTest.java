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
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

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
    private CulturalOfferingRepository repository;
    @Autowired
    private Mapper mapper;

    private PostDto createTestPostDto() {
        PostDto postDto = new PostDto();

        postDto.setContent(PostConstants.TEST_CONTENT);
        postDto.setCulturalOfferingId(PostConstants.TEST_CULTURAL_OFFERING_ID);

        return postDto;
    }

    private PostDto createTestUpdatePostDto(){
        PostDto dto = createTestPostDto();
        dto.setId(PostConstants.EXISTING_ID1);
        return dto;
    }

    @Test
    @Transactional
    public void testReadAllByCulturalOfferingId(){
        Pageable pageRequest = PageRequest.of(0, PostConstants.PAGE_SIZE);

        Page<PostDto> returnedPosts = postService
                .readAllByCulturalOfferingId(CulturalOfferingConstants.EXISTING_ID1, pageRequest);

        assertEquals(16, returnedPosts.getTotalElements());
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
    public void testReadByIdDoesntExist(){
        Optional<PostDto> returnedPost = postService.readById(PostConstants.TEST_ID);

        assertFalse(returnedPost.isPresent());
    }

    @Test
    @Transactional
    public void testSave(){
        PostDto newPost = new PostDto();
        newPost.setContent("CONTENT");
        newPost.setCulturalOfferingId(1L);
        newPost.setTimeAdded(LocalDateTime.now());
        long oldDb = postRepository.count();

        PostDto createdPost = postService.save(newPost);

        long newDb = postRepository.count();

        assertNotNull(createdPost);
        assertEquals(createdPost.getContent(), newPost.getContent());
        assertEquals(oldDb + 1, newDb);

        postRepository.deleteById(createdPost.getId());
    }

    @Test
    @Transactional
    public void testUpdate() {
        Post post = postRepository.findById(1L).get();
        String oldContent = post.getContent();
        PostDto updatePost = new PostDto();
        updatePost.setId(1L);
        updatePost.setTimeAdded(post.getTimeAdded());
        updatePost.setContent("NOVI KONTENT");

        PostDto updatedPost = postService.update(updatePost);

        assertNotNull(updatedPost);
        assertEquals(1L, updatedPost.getId().longValue());
        assertEquals("NOVI KONTENT", updatedPost.getContent());

        post.setContent(oldContent);
        postRepository.save(post);
    }

    @Test
    @Transactional
    public void testDelete() {
        Post newPost = new Post();
        newPost.setContent("KONTENT");
        newPost.setTimeAdded(LocalDateTime.now());
        newPost.setCulturalOffering(repository.getOne(1L));
        newPost = postRepository.save(newPost);

        long oldDb = postRepository.count();

        postRepository.deleteById(newPost.getId());

        long newDb = postRepository.count();

        assertEquals(oldDb - 1, newDb);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteNotFound() {
        postService.delete(45L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateNotFound() {
        PostDto updatePost = new PostDto();
        updatePost.setId(45L);
        updatePost.setContent("NOVI KONTENT");

        postService.update(updatePost);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveExists() {
        PostDto savePost = new PostDto();
        savePost.setId(1L);
        savePost.setContent("NOVI KONTENT");
        savePost.setCulturalOfferingId(1L);

        postService.save(savePost);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void saveOfferingNotFound() {
        PostDto savePost = new PostDto();
        savePost.setContent("NOVI KONTENT");
        savePost.setCulturalOfferingId(54L);

        postService.save(savePost);
    }

    @Test(expected = NullPointerException.class)
    public void saveOfferingNull() {
        PostDto savePost = new PostDto();
        savePost.setContent("NOVI KONTENT");

        postService.save(savePost);
    }
}
