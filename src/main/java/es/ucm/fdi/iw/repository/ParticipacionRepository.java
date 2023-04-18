package es.ucm.fdi.iw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Participacion;
import es.ucm.fdi.iw.model.User;
@Repository
public interface ParticipacionRepository extends JpaRepository <Participacion, Long> {
    
}
