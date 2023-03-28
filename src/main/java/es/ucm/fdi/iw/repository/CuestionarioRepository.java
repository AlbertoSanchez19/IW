package es.ucm.fdi.iw.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Cuestionario;
import es.ucm.fdi.iw.model.User;
@Repository
public interface CuestionarioRepository extends JpaRepository <Cuestionario, Long> {
    public List<Cuestionario> findByTitulo(String titulo);
	public Optional<Cuestionario> findById(long id);

	// @Query("SELECT c.*, u.first_name, u.last_name from CUESTIONARIO c join IWUSER u on c.id_usuario = u.id")
	// @Query("SELECT c.*, u.firstName, u.lastName from Cuestionario c join User u on c.id = u.id")
	// @Query("Select * from Cuestionario")
	public List<Cuestionario> findAll();
	
	// @Query("SELECT c.*, u.first_name, u.last_name from CUESTIONARIO c join IWUSER u on c.id_usuario = u.id where u:profesor")
	public List<Cuestionario> findAllByAutor(User profesor);
	public void deleteById(Long id);


}
