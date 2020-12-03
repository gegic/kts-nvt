package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PostService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private PostService postService;
    private ModelMapper modelMapper;

    public PostController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostDto>> get(@PathVariable String culturalOfferingId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<PostDto> postDtos = this.postService
                .readAllByCulturalOfferingId(Long.parseLong(culturalOfferingId), p)
                .map(post -> modelMapper.map(post, PostDto.class));
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.postService.readById(id).map(post -> modelMapper.map(post, PostDto.class)));
    }

    @PostMapping
    ResponseEntity<PostDto> add(@RequestBody Post Post){
        Post saved = this.postService.save(Post);
        return ResponseEntity.created(URI.create(String.format("/api/post/%s", saved.getId())))
                .body(modelMapper.map(saved, PostDto.class));
    }

    @PutMapping
    ResponseEntity<PostDto> update(@RequestBody Post Post){
        return ResponseEntity.ok(modelMapper.map(this.postService.save(Post), PostDto.class));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.postService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
