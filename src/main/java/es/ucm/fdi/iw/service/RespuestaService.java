package es.ucm.fdi.iw.service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.iw.model.*;
import es.ucm.fdi.iw.repository.*;
public class RespuestaService {
    @Autowired
    private RespuestaRepository respuestaRepository;

    public void guardarRespuestas(List<Respuesta> respuestas) {
        respuestaRepository.saveAll(respuestas);
    }
    
}
