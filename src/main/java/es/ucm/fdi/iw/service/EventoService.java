package es.ucm.fdi.iw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.model.Evento;
import es.ucm.fdi.iw.repository.EventoRepository;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Evento obtenerPorCodigo(String codigo) {
        return eventoRepository.findByCodigo(codigo);
    }
}
