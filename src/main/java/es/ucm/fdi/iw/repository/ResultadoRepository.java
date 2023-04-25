package es.ucm.fdi.iw.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import es.ucm.fdi.iw.model.Respuesta;
import es.ucm.fdi.iw.model.Resultado;
@Repository
public interface ResultadoRepository extends JpaRepository <Resultado, Long> {
	public List<Resultado> findAll();
	public Respuesta findById(long id);
	public void deleteById(Long id);

}
