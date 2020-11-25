package rs.ac.uns.ftn.ktsnvt.kultura.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/user/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> getAll(@RequestBody Pageable p) {
        // TODO DTO and factory soon
        return new ResponseEntity<>(this.userService.readAll(p), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<User> get(@PathVariable String id){
        return new ResponseEntity<>(this.userService.readById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<User> add(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(this.userService.create(user), HttpStatus.CREATED);
    }

    @PutMapping()
    ResponseEntity<User> update(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(this.userService.update(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable String id) throws Exception {
        this.userService.delete(UUID.fromString(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
