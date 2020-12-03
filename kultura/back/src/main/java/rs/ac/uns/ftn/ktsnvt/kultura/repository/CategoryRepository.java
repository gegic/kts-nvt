package rs.ac.uns.ftn.ktsnvt.kultura.repository;

import rs.ac.uns.ftn.ktsnvt.kultura.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
