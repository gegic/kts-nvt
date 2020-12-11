package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.PageableExtractor;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UserService userService;

    @Autowired
    private UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "3") int size,
                                                  @RequestParam(defaultValue = "id,desc") String[] sort) {
        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<UserDto> moderatorsDto = this.userService.readAll(p);
        return ResponseEntity.ok(moderatorsDto);
    }


    @GetMapping(path = "/moderators")
    public ResponseEntity<Page<UserDto>> getModerators(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size,
                                                       @RequestParam(defaultValue = "id,desc") String[] sort) {
        Pageable p = PageableExtractor.extract(page, size, sort);
        Page<UserDto> moderatorsDto = this.userService.readByAuthority(p, "ROLE_MODERATOR");
        return ResponseEntity.ok(moderatorsDto);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> get(@PathVariable UUID id){
        return ResponseEntity.of(this.userService.findById(id));
    }

    @PostMapping
    ResponseEntity<UserDto> add(@Valid @RequestBody UserDto userDto) throws Exception {
        UserDto saved = this.userService.create(userDto);
        return ResponseEntity.created(URI.create("/api/user/" + saved.getId())).body(saved);
    }

    @PostMapping("/moderator")
    ResponseEntity<UserDto> addModerator(@RequestBody UserDto userDto) throws Exception {
        UserDto saved = this.userService.create(userDto, "ROLE_MODERATOR");
        return ResponseEntity.created(URI.create("/api/user/" + saved.getId())).body(saved);
    }

    @PutMapping
    ResponseEntity<UserDto> update(@Valid @RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.ok(this.userService.update(userDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) throws Exception {
        this.userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
