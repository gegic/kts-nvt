package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceExistsException;
import rs.ac.uns.ftn.ktsnvt.kultura.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Mapper;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.AuthorityRepository;
import rs.ac.uns.ftn.ktsnvt.kultura.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final Mapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final AuthorityRepository authorityRepository;
  private final SMTPServer smtpServer;

  @Autowired
  public UserService(
      UserRepository userRepository,
      Mapper mapper,
      PasswordEncoder passwordEncoder,
      AuthorityRepository authorityRepository,
      SMTPServer smtpServer) {
    this.userRepository = userRepository;
    this.mapper = mapper;
    this.passwordEncoder = passwordEncoder;
    this.authorityRepository = authorityRepository;
    this.smtpServer = smtpServer;
  }

  public Page<UserDto> readAll(Pageable p) {
    return userRepository.findAll(p).map(u -> mapper.fromEntity(u, UserDto.class));
  }

  public Page<UserDto> readByAuthority(Pageable p, String authority) {
    return userRepository
        .findByAuthority(authority, p)
        .map(u -> mapper.fromEntity(u, UserDto.class));
  }

  public Optional<UserDto> findById(long id) {
    return userRepository.findById(id).map(u -> mapper.fromEntity(u, UserDto.class));
  }

  public UserDto create(UserDto dto) {
    return this.create(dto, "ROLE_USER");
  }

  @Transactional
  public UserDto create(UserDto dto, String role) {
    if(dto.getId()!=null){
      if(userRepository.existsById(dto.getId())){
        throw new ResourceExistsException("User with id: "+ dto.getId() +"already exists");
      }
    }
    User u = new User();

    u.setPassword(passwordEncoder.encode(dto.getPassword()));
    u.setFirstName(dto.getFirstName());
    u.setLastName(dto.getLastName());
    u.setEmail(dto.getEmail());
    u.setVerified(false);
    Authority a = authorityRepository.findByAuthority(role);
    u.addAuthority(a);
    u = this.userRepository.save(u);
    sendMail(u);
    return mapper.fromEntity(u, UserDto.class);
  }

  public UserDto update(UserDto userDto) {
    User existingUser = userRepository.findById(userDto.getId()).orElse(null);
    if (existingUser == null) {
      throw new ResourceNotFoundException("User with given id doesn't exist");
    }

    User updated = mapper.toExistingEntity(userDto, existingUser);

//    existingUser.setEmail(userDto.getEmail());
//    existingUser.setFirstName(userDto.getFirstName());
//    existingUser.setLastName(userDto.getLastName());
//    existingUser.setVerified(userDto.isVerified());
    if(userDto.getPassword()!=null){
      existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
    existingUser.setLastPasswordChange(LocalDateTime.now());
//    return mapper.fromEntity(userRepository.save(existingUser), UserDto.class);
    return mapper.fromEntity(userRepository.save(updated), UserDto.class);
  }

  public void delete(long id) throws Exception {
    User existingUser = userRepository.findById(id).orElse(null);
    if (existingUser == null) {
      throw new ResourceNotFoundException("User with given id doesn't exist");
    }
    userRepository.delete(existingUser);
  }

  @Override
  public User loadUserByUsername(String s) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(s)
        .orElseThrow(() -> new UsernameNotFoundException(s));
  }

  public Optional<UserDto> findByEmail(String email) {
    return userRepository.findByEmail(email).map(e -> mapper.fromEntity(e, UserDto.class));
  }

  public void sendMail(User user) {
    String link = String
            .format("<br>Nalog je potrebno verifikovati klikom na " +
                    "<a href=\"http:/localhost:4200/verify/%s\">ovaj link</a>.", user.getId());
    String body = String.format("Pozdrav,<br>%s, uspešno ste kreirali nalog.", user.getFirstName()) + link;
    try {
      this.smtpServer.sendEmail(user.getEmail(), "Uspešno kreiran nalog", body);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public UserDto verify(long id) throws Exception {
    User existingUser = userRepository.findById(id).orElse(null);
    if (existingUser == null) {
      throw new ResourceNotFoundException("User with the given id doesn't exist");
    }
    existingUser.setVerified(true);
    return  mapper.fromEntity(userRepository.save(existingUser), UserDto.class);
  }


}
