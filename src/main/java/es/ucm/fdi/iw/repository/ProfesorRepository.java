package es.ucm.fdi.iw.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Cuestionario;
import es.ucm.fdi.iw.model.User;
@Repository
public interface ProfesorRepository extends JpaRepository <User, Long> {
	public Optional<User> findById(long id);
	public List<User> findByRolesContaining(String role);
	public void deleteById(Long id);
}
