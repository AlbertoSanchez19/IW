package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.service.CuestionarioService;
import lombok.extern.java.Log;
import es.ucm.fdi.iw.model.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    private CuestionarioRepository cuestionarioRepository;
    @Autowired
	private EntityManager entityManager;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private ParticipacionRepository participacionRepository;
	

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    @GetMapping("/profesor")
    public String profesorMain(Model model) {
        return "Profesor";
    }@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @Transactional
    @PostMapping("/register")
    public String registered(@ModelAttribute("user") User user, @RequestParam("classInput") String classInput) {
        user.setRoles("USER");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Clases clase = claseRepository.findByNombre(classInput);

        if (clase == null) {
            // Maneja el caso en que la clase no existe
            throw new RuntimeException("La clase no existe: " + classInput);
        }
        Participacion participacion = new Participacion();
        participacion.setUsuario(user);
        participacion.setClase(clase);
        participacionRepository.save(participacion);
        entityManager.persist(user);
        entityManager.flush();
        return "login";
    }

    

    @GetMapping("/info_profesor")
    public String admin_info_profesor(Model model) {
        return "admin_info_profesor";

    }

    @GetMapping("/marcador")
    public String marcador(Model model) {
        return "marcador";

    }

    @GetMapping("/quizz_link")
    public String quizz_link(Model model) {
        return "quizz_link";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        
        List<Cuestionario> cuestionarios = cuestionarioRepository.findAll();
        log.info(cuestionarios.toString());
        model.addAttribute("cuestionarios", cuestionarios);
        return "catalogo";
    }
    @GetMapping("/PIN")
    public String introducirPin(Model model) {
        return "introducir_pin";
    }
    @PostMapping("/PIN")
    public String PinRegistro(@ModelAttribute("user") User user, @RequestParam("classInput") String classInput) {
        user.setRoles("USER_NOREG");
        user.setEnabled(true);
        Clases clase = claseRepository.findByNombre(classInput);
        if (clase == null) {
            // Maneja el caso en que la clase no existe
            throw new RuntimeException("La clase no existe: " + classInput);
        }
        Participacion participacion = new Participacion();
        participacion.setUsuario(user);
        participacion.setClase(clase);
        participacionRepository.save(participacion);
        entityManager.persist(user);
        entityManager.flush();
        return "redirect:/PIN";
    }
	@GetMapping("/PIN_log")
    public String introducirPinlogeado(Model model) {
        return "introducir_pin_logeado";
    }

}
