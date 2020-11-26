package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
