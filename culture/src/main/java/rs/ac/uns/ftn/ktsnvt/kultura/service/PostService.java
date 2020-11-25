package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CategoryRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    public Page<Post> readAllByCulturalOfferingId(UUID culturalOfferingId, Pageable p) {
        return postRepository.findAllByCulturalOfferingId(culturalOfferingId, p);
    }

    public Post save(Post p) {
        return postRepository.save(p);
    }

    public void delete(UUID id) {
        postRepository.deleteById(id);
    }
}
