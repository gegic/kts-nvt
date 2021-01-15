package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class PostService {


    private final PostRepository postRepository;
    private final Mapper mapper;

    @Autowired
    public PostService(PostRepository postRepository, Mapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    public Page<PostDto> readAllByCulturalOfferingId(long culturalOfferingId, Pageable p) {
        return postRepository.findAllByCulturalOfferingId(culturalOfferingId, p)
                .map(post -> mapper.fromEntity(post, PostDto.class));
    }

    @Transactional
    public Optional<PostDto> readById(long id) {
        return postRepository.findById(id).map(post -> mapper.fromEntity(post, PostDto.class));
    }

    @Transactional
    public PostDto update(PostDto p){
        ResourceNotFoundException exc = new ResourceNotFoundException("A post with ID " + p.getId() + " doesn't exist!");

        Post toUpdate = postRepository.findById(p.getId()).orElseThrow(() -> exc );

        return mapper.fromEntity(postRepository.save(mapper.toExistingEntity(p, toUpdate)), PostDto.class);
    }

    public PostDto save(PostDto p) {
        return mapper.fromEntity(postRepository.save(mapper.fromDto(p, Post.class)), PostDto.class);
    }

    public void delete(long id) {
        postRepository.deleteById(id);
    }
}
