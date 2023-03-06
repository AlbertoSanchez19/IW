package es.ucm.fdi.iw.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Cuestionario;
@Repository
public interface CuestionarioRepository extends JpaRepository <Cuestionario, Long> {
    public List<Cuestionario> findByTitulo(String titulo);
	public List<Cuestionario> findById(long id);
	public List<Cuestionario> findAll();

	public void deleteById(Long id);
}
