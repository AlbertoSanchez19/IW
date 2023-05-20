package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Clases;
import es.ucm.fdi.iw.model.Cuestionario;
import es.ucm.fdi.iw.model.Participacion;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.repository.ClaseRepository;
import es.ucm.fdi.iw.repository.CuestionarioRepository;
import es.ucm.fdi.iw.repository.ParticipacionRepository;
import es.ucm.fdi.iw.service.ClaseService;

@Controller
@RequestMapping("clases")
public class ClaseController {

    private static final Logger log = LogManager.getLogger(CuestionarioController.class);
    @Autowired
    private HttpSession session;

    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    @Autowired
    private ParticipacionRepository participacionRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @GetMapping("/marcador")
    public String marcador(Model model) {
        return "marcador";

    }

    @GetMapping("/quizz_link")
    public String quizz_link(Model model) {
        return "quizzLink";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {

        List<Cuestionario> cuestionarios = cuestionarioRepository.findAll();
        log.info(cuestionarios.toString());
        model.addAttribute("cuestionarios", cuestionarios);
        return "catalogo";
    }

    @GetMapping("/listaclases")
    public String listaclases(Model model) {
        String previousUrl = "/clases/listaclases";
        session.setAttribute("previousUrl", previousUrl);

        List<Clases> clases = claseRepository.findAll();
        model.addAttribute("clases", clases);
        return "listaClase";
    }

    @GetMapping("/PIN")
    public String introducirPin(Model model) {
        return "introducirPin";
    }

    @GetMapping("/crearclase")
    public String crearClase(Model model) {
        return "crearClase";
    }

    @PostMapping("/crearclase")
    public RedirectView crearClaseSubmit(@RequestParam("nombre") String nombre, Model model,
            HttpSession session) {
        Clases clase = new Clases();
        clase.setNombre(nombre);
        claseRepository.save(clase);
        Participacion participacion = new Participacion();
        participacion.setClase(clase);
        participacion.setUsuario((User) session.getAttribute("u"));
        participacionRepository.save(participacion);
        return new RedirectView((String) session.getAttribute("previousUrl"));
        // return "redirect:/clases/seleccionClases";
    }

    @GetMapping("/crearclase2")
    public String crearClase2(Model model) {
        return "crearClase2";
    }

    @PostMapping("/crearclase2")
    public String crearClaseSubmit2(@RequestParam("nombre") String nombre, Model model) {
        Clases clase = new Clases();
        clase.setNombre(nombre);
        claseRepository.save(clase);
        Participacion participacion = new Participacion();
        participacion.setClase(clase);
        participacion.setUsuario((User) session.getAttribute("u"));
        participacionRepository.save(participacion);
        return "redirect:/clases/listaclases";
    }

}
