package es.ucm.fdi.iw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private CuestionarioService cuestionarioService;

    @GetMapping("/profesores")
    public String profesores(Model model){
        List<User> listaProfesores = profesorService.obtenerProfesores();
        model.addAttribute("profesores", listaProfesores);
        return "admin_profesores";
    }

    @GetMapping("/profesores/nuevo")
    public String listarUsuarios(Model model){
        List<User> usuarios = profesorService.obtenerUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "nuevo_profesor";
    }

    @PostMapping("/usuarios/{id}/rol")
    public String actualizarRol(@PathVariable("id") Long id, @RequestParam("rol") String rol) {
        Optional<User> optionalUsuario = profesorService.obtenerPorId(id);
        if (optionalUsuario.isPresent()) {
            User usuario = optionalUsuario.get();
            String roles = usuario.getRoles();
            roles = roles + ",PROFESOR";
            usuario.setRoles(roles);
            profesorService.guardarProfesor(usuario);
        }
        return "redirect:/profesores";
}

    @PostMapping("/profesores/{profesorId}/info_profesor")
    public String infoProfesor(@PathVariable Long profesorId, Model model) throws NotFoundException{
        User profesor = profesorService.obtenerPorId(profesorId)
                .orElseThrow(() -> new NotFoundException());
        
        // Solamente para ver si se muestran los cuestinoarios
        Cuestionario cuest = new Cuestionario();
        cuest.setUsuario(profesor);
        cuest.setTitulo("prueba");
        cuestionarioService.save(cuest);
        List<Cuestionario> cuestionarios = cuestionarioService.obtenerCuestionarios(profesor);
        
        model.addAttribute("profesor", profesor);
        model.addAttribute("cuestionarios", cuestionarios);
        return "admin_info_profesor";
    }
    
}
