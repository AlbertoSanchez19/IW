package es.ucm.fdi.iw.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import es.ucm.fdi.iw.model.Respuesta;
@Repository
public interface RespuestaRepository extends JpaRepository <Respuesta, Long> {
	public List<Respuesta> findAll();

	public void deleteById(Long id);
}
