package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Post;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PostService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PostService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/post/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping(path = "/{culturalOfferingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Post>> get(@PathVariable String culturalOfferingId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @RequestParam(defaultValue = "id,desc") String[] sort){

        Pageable p = PageableExtractor.extract(page, size, sort);
        return ResponseEntity.ok(this.postService.readAllByCulturalOfferingId(UUID.fromString(culturalOfferingId), p));
    }

    @GetMapping(path="{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> getById(@PathVariable UUID id) {
        return ResponseEntity.of(this.postService.readById(id));
    }

    @PostMapping
    ResponseEntity<Post> add(@RequestBody Post Post){
        Post saved = this.postService.save(Post);
        return ResponseEntity.created(URI.create(String.format("/api/post/%s", saved.getId()))).body(saved);
    }

    @PutMapping
    ResponseEntity<Post> update(@RequestBody Post Post){
        return ResponseEntity.ok(this.postService.save(Post));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id){
        this.postService.delete(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

}
