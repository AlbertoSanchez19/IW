package es.ucm.fdi.iw.service;

import org.springframework.beans.factory.annotation.Autowired;

import es.ucm.fdi.iw.repository.EventoRepository;

public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;
}
