package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
public class CrearCuestionarioController {
    @GetMapping("/OC")
    public String opcionesCreado(Model model) {
        return "OpcionesCreado";
    }
    @GetMapping("/CP")
    public String creacionPreguntas(Model model) {
        return "creacionPreguntas";
    }
}
