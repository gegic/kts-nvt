package rs.ac.uns.ftn.ktsnvt.kultura.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.StringDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.TokenResponse;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;
import rs.ac.uns.ftn.ktsnvt.kultura.utils.TokenUtils;

import java.util.Optional;


@RestController
@RequestMapping(path = "/auth")
public class AuthController {


    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    Mapper mapper;

    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginInfo){
        try{
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getEmail(),
                            loginInfo.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = TokenUtils.generateToken(user);

            return ResponseEntity.ok(new TokenResponse(jwt, mapper.fromEntity(user, UserDto.class)));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path="/exists/email/{email}")
    public ResponseEntity<StringDto> existsByEmail(@PathVariable String email) {
        Optional<UserDto> userDto = userService.findByEmail(email);
        if(userDto.isPresent()) {
            UserDto userDto1 = userDto.get();
            return ResponseEntity.ok(new StringDto(
                    String.format("%s %s", userDto1.getFirstName(), userDto1.getLastName())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/exists/email/id/{email}")
    public ResponseEntity<StringDto> getIdByExistsMail(@PathVariable String email) {
        Optional<UserDto> userDto = userService.findByEmail(email);
        if(userDto.isPresent()) {
            UserDto userDto1 = userDto.get();
            return ResponseEntity.ok(new StringDto(
                    String.format("%s", userDto1.getId())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path="/exists/verify/id/{id}")
    public ResponseEntity<StringDto> existsById(@PathVariable long id) {
        Optional<UserDto> userDto = userService.findById(id);
        if(userDto.isPresent() && !userDto.get().isVerified()) {
            UserDto userDto1 = userDto.get();
            return ResponseEntity.ok(new StringDto(
                    String.format("%s %s", userDto1.getFirstName(), userDto1.getLastName())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path="/register")
    public ResponseEntity<Void> register(@RequestBody UserDto userDto) {
        try {
            userService.create(userDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/verify/{id}")
    public ResponseEntity<UserDto> verify(@PathVariable long id) throws Exception {
        UserDto activateUser = this.userService.verify(id);
        return ResponseEntity.ok(activateUser);
    }

}
