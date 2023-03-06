package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.model.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.repository.query.Param;


/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
public class CrearCuestionarioController {
    
        
        @Autowired
        private CuestionarioRepository cuestionarioRepository;
        
        @Autowired
        private PreguntaRepository preguntaRepository;
        
        @PostMapping
        public Cuestionario crearCuestionario(@RequestBody Cuestionario cuestionario) {
            return cuestionarioRepository.save(cuestionario);
        }
        
        @PostMapping("/{idCuestionario}/preguntas")
        public Pregunta agregarPregunta(@PathVariable Long idCuestionario, @RequestBody Pregunta pregunta) throws NotFoundException{
            Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario).orElseThrow(() -> new NotFoundException());
            pregunta.setCuestionario(cuestionario);
            return preguntaRepository.save(pregunta);
        }
        
        // otros métodos para obtener cuestionarios, preguntas, respuestas, etc.
        
    

}
