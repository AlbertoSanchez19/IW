package es.ucm.fdi.iw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Clases;
import es.ucm.fdi.iw.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    public Evento findByCodigo(String codigo);

    public List<Evento> findAll();

}