package es.ucm.fdi.iw.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.service.CuestionarioService;
import es.ucm.fdi.iw.service.ProfesorService;
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


/**
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
public class ProfesorController {

    private static final Logger log = LogManager.getLogger(ProfesorController.class);

    @Autowired
    private ProfesorService profesorService;

    @GetMapping("/profesores")
    public String profesores(Model model){
        List<User> listaProfesores = profesorService.obtenerProfesores();
        model.addAttribute("profesores", listaProfesores);
        return "admin_profesores";
    }

    @GetMapping("/profesores/nuevo")
    public String nuevoProfesor(Model model){
        User profesor = new User();
        model.addAttribute("profesor", profesor);
        return "nuevo_profesor";
    }

    @PostMapping("/profesores")
    public String guardarProfesor(@ModelAttribute("profesor") User profesor){
        profesor.setId(0);
        profesor.setEnabled(true);
        profesor.setRoles("USER,PROFESOR");
        profesorService.guardarProfesor(profesor);
        return "redirect:/profesores";
    }
    
}
