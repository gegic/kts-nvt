package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import java.util.Optional;
import java.util.Set;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;
    @Autowired
    public UserService(UserRepository userRepository,
                       Mapper mapper,
                       PasswordEncoder passwordEncoder,
                       AuthorityService authorityService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityService = authorityService;
    }

    public Page<UserDto> readAll(Pageable p) {
        return userRepository.findAll(p).map(u -> mapper.fromEntity(u, UserDto.class));
    }

//    public Page<User> readByType(Pageable p, String authority) {
//
//    }

    public Optional<UserDto> readById(long id) {
        return userRepository.findById(id).map(u -> mapper.fromEntity(u, UserDto.class));
    }

    public UserDto create(UserDto dto) throws Exception {
        if(!userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new Exception("User with given email address already exists");
        }
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setEmail(dto.getEmail());

        Set<Authority> auth = authorityService.findByName("ROLE_USER");
        u.setAuthorities(auth);

        u = this.userRepository.save(u);
        return mapper.fromEntity(u, UserDto.class);
    }

    public UserDto update(UserDto userDto) throws Exception {
        User existingUser =  userRepository.findById(userDto.getId()).orElse(null);
        if(existingUser == null){
            throw new Exception("User with given id doesn't exist");
        }
        existingUser.setPassword(userDto.getPassword());
        return mapper.fromEntity(userRepository.save(existingUser), UserDto.class);
    }

    public void delete(long id) throws Exception {
        User existingUser = userRepository.findById(id).orElse(null);
        if(existingUser == null){
            throw new Exception("User with given id doesn't exist");
        }
        userRepository.delete(existingUser);
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByEmailUsername(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
