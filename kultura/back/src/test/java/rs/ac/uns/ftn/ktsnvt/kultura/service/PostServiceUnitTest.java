package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.constants.PostConstants;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;

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

    @MockBean
    Mapper mapper;

    @Autowired
    PostService postService;
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

    private Post createExistingPost(){
        Post p = new Post();
        p.setContent(PostConstants.EXISTING_CONTENT1);
        p.setId(PostConstants.EXISTING_ID1);
        return p;
    }

    @Test
    public void testUpdate(){

        PostDto update = createTestUpdatePostDto();

        Post existingPost = createExistingPost();

        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(existingPost));
        Mockito
                .when(mapper.toExistingEntity(Mockito.any(), Mockito.any()))
                .thenAnswer((i)->{
                    Post p = i.getArgument(1);
                    PostDto dto = i.getArgument(0);
                    p.setContent(dto.getContent());
                    p.setTimeAdded(dto.getTimeAdded());
                    return p;
                });
        Mockito.when(postRepository.save(Mockito.any())).thenAnswer((i)->i.getArgument(0));
        Mockito
                .when(mapper.fromEntity(Mockito.any(), Mockito.any()))
                .thenAnswer((i)->{
                    PostDto dto = new PostDto();
                    Post p = i.getArgument(0);
                    dto.setId(p.getId());
                    dto.setContent(p.getContent());
                    dto.setTimeAdded(p.getTimeAdded());
                    return dto;
                });

        PostDto resultPost = postService.update(update);
        assertNotNull(resultPost);
        assertEquals(resultPost.getContent(), update.getContent());
        assertEquals(resultPost.getId(), update.getId());
    }
    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateNotFound(){
        Mockito.when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        PostDto postDto = createTestPostDto();
        postService.update(postDto);
    }

}
