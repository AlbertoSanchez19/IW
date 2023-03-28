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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.repository.query.Param;
import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
public class CuestionarioController {

    private static final Logger log = LogManager.getLogger(CuestionarioController.class);

    @Autowired
    private HttpSession session;

    @Autowired
    private LocalData localData;

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
            // @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
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
        cuestionario.setUsuario((User) session.getAttribute("u"));
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

    /**
     * Downloads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        File f = localData.getFile("respuesta", ""+id+".jpg");
        InputStream in = new BufferedInputStream(f.exists() ?
            new FileInputStream(f) : null);
        return os -> FileCopyUtils.copy(in, os);
    }

    /**
     * Uploads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("{id}/pic")
	@ResponseBody
    public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id, 
        HttpServletResponse response, HttpSession session, Model model) throws IOException {

        Respuesta target = respuestaRepository.findById(id);
        model.addAttribute("user", target);
		
		log.info("Updating photo for respuesta {}", id);
		File f = localData.getFile("respuesta", ""+id+".jpg");
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
                log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "{\"status\":\"photo uploaded correctly\"}";
    }
   


