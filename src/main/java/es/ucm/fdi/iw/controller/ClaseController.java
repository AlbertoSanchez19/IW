package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.service.CuestionarioService;
import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.model.*;
import es.ucm.fdi.iw.service.*;

@Controller
@RequestMapping("clases")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    private static final Logger log = LogManager.getLogger(CuestionarioController.class);
    @Autowired
    private HttpSession session;

    @Autowired
    private LocalData localData;

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
        return "quizz_link";
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
        return "listaclase";
    }

    @GetMapping("/PIN")
    public String introducirPin(Model model) {
        return "introducir_pin";
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
}
