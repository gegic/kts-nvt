package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.PostDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PostService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;


@RestController
@RequestMapping(path = "/api/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostDto>> get(@PathVariable long culturalOfferingId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "3") int size,
                                              @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<PostDto> postDtos = this.postService
                .readAllByCulturalOfferingId(culturalOfferingId, p);
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostDto> getById(@PathVariable long id) {
        return ResponseEntity.of(this.postService.readById(id));
    }

    @PostMapping
    ResponseEntity<PostDto> add(@RequestBody PostDto postDto){
        PostDto saved = this.postService.save(postDto);
        return ResponseEntity.created(URI.create(String.format("/api/post/%s", saved.getId())))
                .body(saved);
    }

    @PutMapping
    ResponseEntity<PostDto> update(@RequestBody PostDto postDto){
        return ResponseEntity.ok(this.postService.save(postDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.postService.delete(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }

}
