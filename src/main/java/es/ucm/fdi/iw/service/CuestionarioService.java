package es.ucm.fdi.iw.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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

    public List<Cuestionario> obtenerCuestionarios(User profesor) {
        return cuestionarioRepository.findAllByAutor(profesor);
    }

    public void eliminarCuestionario(Long cuestionarioId) throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
                .orElseThrow(() -> new NotFoundException());

        // Obtener todas las preguntas asociadas al cuestionario
        List<Pregunta> preguntas = preguntaRepository.findByCuestionario(cuestionario);

        // Eliminar todas las respuestas asociadas a las preguntas
        for (Pregunta pregunta : preguntas) {
            List<Respuesta> respuestas = respuestaRepository.findByPregunta(pregunta);
            respuestaRepository.deleteAll(respuestas);
        }

        // Eliminar todas las preguntas
        preguntaRepository.deleteAll(preguntas);

        // Eliminar el cuestionario
        cuestionarioRepository.delete(cuestionario);

    }
    // otros métodos para validar respuestas, obtener estadísticas, etc.

}
