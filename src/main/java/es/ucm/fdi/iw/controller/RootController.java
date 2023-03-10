package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
    @GetMapping("/RP")
    public String responderPreguntas(Model model) {
        return "responderPreguntas";
    }
    @GetMapping("/PIN")
    public String introducirPin(Model model) {
        return "introducir_pin";
    }

    
    @GetMapping("/admin_profesores")
    public String admin_profesores(Model model) {
        return "admin_profesores";

    }

    @GetMapping("/admin_info_profesor")
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
        return "catalogo";
    }
    
}
