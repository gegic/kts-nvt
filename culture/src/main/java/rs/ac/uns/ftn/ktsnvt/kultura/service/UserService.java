package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired // will be used in spring security
//    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private AuthorityService authService;

    public Page<User> readAll(Pageable p) {
        return userRepository.findAll(p);
    }

    public User readById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User create(User entity) throws Exception {
        if(!userRepository.findByEmail(entity.getEmail()).isPresent()){
            throw new Exception("User with given email address already exists");
        }
        User u = new User();
        u.setUsername(entity.getUsername());
        // pre nego sto postavimo lozinku u atribut hesiramo je
//        u.setPassword(passwordEncoder.encode(entity.getPassword()));
        u.setFirstName(entity.getFirstName());
        u.setLastName(entity.getLastName());
        u.setEmail(entity.getEmail());

//        List<Authority> auth = authService.findByName("ROLE_USER");
        // u primeru se registruju samo obicni korisnici i u skladu sa tim im se i dodeljuje samo rola USER
//        u.setAuthorities(auth);

        u = this.userRepository.save(u);
        return u;
    }

    public User update(User entity) throws Exception {
        User existingUser =  userRepository.findById(entity.getId()).orElse(null);
        if(existingUser == null){
            throw new Exception("User with given id doesn't exist");
        }
        existingUser.setPassword(entity.getPassword());
        return userRepository.save(existingUser);
    }

    public void delete(UUID id) throws Exception {
        User existingUser = userRepository.findById(id).orElse(null);
        if(existingUser == null){
            throw new Exception("User with given id doesn't exist");
        }
        userRepository.delete(existingUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

}
