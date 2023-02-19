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


    /**
     * @param model
     * @return
     */
    @GetMapping("/CP1")
    public String CP1(Model model) {
        return "CreaPregunta1";
    }

    @GetMapping("/admin_profesores")
    public String admin_profesores(Model model) {
        return "admin_profesores";

    }

    @GetMapping("/admin_info_profesor")
    public String admin_info_profesor(Model model) {
        return "admin_info_profesor";

    }
}
