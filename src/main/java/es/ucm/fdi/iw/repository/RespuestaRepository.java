package es.ucm.fdi.iw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import es.ucm.fdi.iw.model.*;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
	public List<Respuesta> findAll();

	public Respuesta findById(long id);

	public void deleteById(Long id);

	public List<Respuesta> findByPregunta(Pregunta pregunta);

}
