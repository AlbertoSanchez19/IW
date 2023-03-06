package es.ucm.fdi.iw.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Cuestionario;
import es.ucm.fdi.iw.model.Pregunta;
import es.ucm.fdi.iw.model.PreguntaType;
@Repository
public interface PreguntaRepository extends JpaRepository <Pregunta, Long> {
    public List<Pregunta> findByTitulo(String tituto);
	public List<Pregunta> findByCuestionario(Cuestionario Cuestionario);
	public List<Pregunta> findByType (PreguntaType tipo);

	public List<Pregunta> findAll();

	public void deleteById(Long id);
}
