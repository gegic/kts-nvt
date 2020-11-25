package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import rs.ac.uns.ftn.ktsnvt.kultura.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
