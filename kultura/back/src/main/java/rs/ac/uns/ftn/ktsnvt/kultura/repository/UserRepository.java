package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.ktsnvt.kultura.model.Authority;
import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = :username or u.username = :username")
    Optional<User> findByEmailUsername(String username);
}
