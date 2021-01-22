package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Before;
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
import rs.ac.uns.ftn.ktsnvt.kultura.constants.PostConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.CulturalOfferingDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Subcategory;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.HelperPage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@Rollback(false)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
public class PostServiceUnitTest {
    @MockBean
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @MockBean
    CulturalOfferingRepository culturalOfferingRepository;

    @MockBean
    Mapper mapper;

    @Before
    public void setupMapper() {
        Mockito.when(mapper.fromDto(Mockito.any(PostDto.class), Mockito.eq(Post.class))).thenAnswer(i -> {
            PostDto dto = i.getArgument(0);
            Post post = new Post();
            if (dto.getId() != null) {
                post.setId(dto.getId());
            }
            if (dto.getCulturalOfferingId() != null) {
                CulturalOffering co = new CulturalOffering();
                co.setId(dto.getCulturalOfferingId());
                co.setName("name");
                post.setCulturalOffering(co);
            }
            post.setContent(dto.getContent());
            return post;
        });

        Mockito.when(mapper.fromEntity(Mockito.any(Post.class), Mockito.eq(PostDto.class))).thenAnswer(i -> {
            Post post = i.getArgument(0);
            PostDto dto = new PostDto();

            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setTimeAdded(post.getTimeAdded());
            if (post.getCulturalOffering() != null) {
                dto.setCulturalOfferingId(post.getCulturalOffering().getId());
            }

            return dto;
        });

        Mockito.when(mapper.toExistingEntity(Mockito.any(PostDto.class), Mockito.any(Post.class))).thenAnswer(i -> {
            Post post = i.getArgument(1);
            PostDto dto = i.getArgument(0);
            if (dto.getId() != null) {
                post.setId(dto.getId());
            }
            post.setContent(dto.getContent());
            return post;
        });
    }

    @Test
    public void testUpdate(){

        PostDto update = new PostDto();
        update.setId(1L);
        update.setContent("KONTENT");
        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setContent("OLD KONTENT");
        existingPost.setTimeAdded(LocalDateTime.now());
        CulturalOffering co = new CulturalOffering();
        co.setId(1L);
        co.setName("CO");
        existingPost.setCulturalOffering(co);

        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(existingPost));

        Mockito.when(postRepository.save(Mockito.any(Post.class))).thenAnswer((i)->i.getArgument(0));
        PostDto resultPost = postService.update(update);
        assertNotNull(resultPost);
        assertEquals(resultPost.getContent(), update.getContent());
        assertEquals(resultPost.getId(), update.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNotFound(){
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setContent("KONTENT");
        postService.update(postDto);
    }

    @Test
    public void testReadById() {
        Post post = new Post();
        post.setId(1L);
        post.setContent("KONTENT");
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(post));

        PostDto postDto = postService.readById(1L).get();

        assertEquals(post.getId(), postDto.getId().longValue());
        assertEquals(post.getContent(), postDto.getContent());
    }

    @Test
    public void testReadAll() {
        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setId(1L);
        post.setContent("KONTENT");
        posts.add(post);
        Mockito.when(postRepository.findAllByCulturalOfferingId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenAnswer(i -> new PageImpl<Post>(posts, PageRequest.of(0, 5), 1));

        Page<PostDto> postPage = postService.readAllByCulturalOfferingId(1L, PageRequest.of(0, 5));

        assertEquals(1, postPage.getTotalElements());
        assertEquals(1, postPage.getContent().size());
    }

    @Test
    public void testSave() {
        PostDto post = new PostDto();
        post.setContent("KONTENT");
        post.setCulturalOfferingId(1L);
        Mockito.when(postRepository.save(Mockito.any(Post.class)))
                .thenAnswer(i -> {
                    Post p = i.getArgument(0);
                    p.setId(1L);
                    return p;
                });
        Mockito.when(culturalOfferingRepository.existsById(1L)).thenReturn(true);

        PostDto saved = postService.save(post);

        assertEquals(1, saved.getId().longValue());
        assertEquals(post.getContent(), saved.getContent());
    }
}
