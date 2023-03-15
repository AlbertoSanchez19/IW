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
	public List<Cuestionario> findAll();
	public List<Cuestionario> findAllByUsuario(User profesor);
	public void deleteById(Long id);
}
