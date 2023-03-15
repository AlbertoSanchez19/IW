package es.ucm.fdi.iw.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.iw.model.*;
import es.ucm.fdi.iw.repository.*;


@Service
public class CuestionarioService {
    
    @Autowired
    private CuestionarioRepository cuestionarioRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;
    
    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private CuestionarioRepository repository;
    
    public List<Pregunta> obtenerPreguntasDeCuestionario(Cuestionario cuestionario) {
        return preguntaRepository.findByCuestionario(cuestionario);
    }
    
    public void guardarRespuestas(List<Respuesta> respuestas) {
        respuestaRepository.saveAll(respuestas);
    }
    public Cuestionario save(Cuestionario cuestionario) {
		return repository.save(cuestionario);
	}

    public List<Cuestionario> obtenerCuestionarios(User profesor){
        return cuestionarioRepository.findAllByUsuario(profesor);
    }
    // otros métodos para validar respuestas, obtener estadísticas, etc.
    
}
