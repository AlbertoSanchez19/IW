package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.service.CuestionarioService;
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
import org.json.*;
import java.util.*;
/**
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
public class CuestionarioController {

    private static final Logger log = LogManager.getLogger(CuestionarioController.class);

    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    @Autowired
    private CuestionarioService cuestionarioService;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @PostMapping
    public Cuestionario crearCuestionario(@RequestBody Cuestionario cuestionario) {
        return cuestionarioRepository.save(cuestionario);
    }

    @PostMapping("/{idCuestionario}/CP")
    public String agregarPregunta(Pregunta pregunta, @RequestParam String jsonRespuestas,
            @PathVariable Long idCuestionario)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        pregunta.setCuestionario(cuestionario);
        Pregunta p = preguntaRepository.save(pregunta);

        JSONArray json = new JSONArray(jsonRespuestas);
        for (int i = 0; i < json.length(); i++) {
            JSONObject rJSON = json.getJSONObject(i);

            Respuesta r = new Respuesta();
            r.setPregunta(p);
            r.setNota(rJSON.getFloat("nota"));
            r.setRespuesta(rJSON.getString("respuesta"));
             
            respuestaRepository.save(r);
            
        }

        return "redirect:/" + cuestionario.getId() + "/CP";
    }

    @PostMapping("/CC")
    public String addCuestionario(@ModelAttribute("cuestionario") Cuestionario cuestionario) {
        log.info("crear cuestionario");
        Cuestionario c = cuestionarioService.save(cuestionario);
        return "redirect:/" + c.getId() + "/CP";
    }

    @GetMapping("/CC")
    public String newCuestionario(Model model) {
        model.addAttribute("cuestionario", new Cuestionario());
        return "CrearCuestionario";
    }

    @GetMapping("/OC")
    public String opcionesCreado(Model model) {
        return "OpcionesCreado";
    }

    @GetMapping("/{idCuestionario}/CP")
    public String creacionPreguntas(Model model, @PathVariable long idCuestionario) throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        return "creacionPreguntas";
    }

    @GetMapping("/{idCuestionario}/RP")
    public String responderPreguntas(Model model, @PathVariable long idCuestionario) throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        
        List<Pregunta> preguntas = preguntaRepository.findByCuestionario(cuestionario);
        model.addAttribute("preguntas", preguntas);
        return "responderPreguntas";
    }



}
