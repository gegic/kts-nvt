package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u join u.authorities auth where auth.authority = :authority")
    Page<User> findByAuthority(String authority, Pageable p);
}
