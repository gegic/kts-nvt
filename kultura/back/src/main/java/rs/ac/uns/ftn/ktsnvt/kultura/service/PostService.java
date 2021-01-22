package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import rs.ac.uns.ftn.ktsnvt.kultura.model.CulturalOffering;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.CulturalOfferingRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class PostService {


    private final PostRepository postRepository;
    private final CulturalOfferingRepository culturalOfferingRepository;
    private final Mapper mapper;
    private final SMTPServer smtpServer;

    @Autowired
    public PostService(PostRepository postRepository,
                       Mapper mapper,
                       CulturalOfferingRepository culturalOfferingRepository,
                       SMTPServer smtpServer) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.culturalOfferingRepository = culturalOfferingRepository;
        this.smtpServer = smtpServer;

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
        if (p.getId() != null)
            throw new IllegalArgumentException("Id has to be null in order for post to be added.");

        if (p.getCulturalOfferingId() == null) {
            throw new NullPointerException("Cultural offering id is null");
        }

        if (!this.culturalOfferingRepository.existsById(p.getCulturalOfferingId())) {
            throw new ResourceNotFoundException("Cultural offering with given id not found");
        }
        Post post = postRepository.save(mapper.fromDto(p, Post.class));
        CulturalOffering co = post.getCulturalOffering();
        co.getSubscribedUsers().parallelStream().forEach(u -> sendMail(u, co));
        return mapper.fromEntity(post, PostDto.class);
    }

    @Transactional
    public void delete(long id) {
        Post p = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with the given id not found"));
        CulturalOffering co = p.getCulturalOffering();
        co.removePost(p);
        culturalOfferingRepository.save(co);
        postRepository.delete(p);
    }

    private void sendMail(User user, CulturalOffering co) {
        String body = String
                .format("<Greetings,<br>There is a new announcement " +
                        "regarding a cultural offering (%s) you're subscribed to. " +
                        "You can access it by clicking " +
                        "<a href=\"http:/localhost:4200/cultural-offering/%s/posts\">on this link</a>.",
                        co.getName(), co.getId());
        try {
            this.smtpServer.sendEmail(user.getEmail(), "New announcement", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
