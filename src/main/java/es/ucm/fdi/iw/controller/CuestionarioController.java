package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import es.ucm.fdi.iw.repository.*;
import es.ucm.fdi.iw.service.ClaseService;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.json.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("cuestionario")
public class CuestionarioController {

    private static final Logger log = LogManager.getLogger(CuestionarioController.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private ClaseService claseService;

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

    @Autowired
    private EventoRepository eventoRepository;

    @PostMapping
    public Cuestionario crearCuestionario(@RequestBody Cuestionario cuestionario) {
        return cuestionarioRepository.save(cuestionario);
    }

    @PostMapping("/{idCuestionario}/{idPregunta}/crearpregunta")
    public String agregarPregunta(Pregunta pregunta, @RequestParam String jsonRespuestas,
            @PathVariable Long idCuestionario, @PathVariable Long idPregunta)
            // @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        pregunta.setCuestionario(cuestionario);
        Pregunta p = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new NotFoundException());
        p.setTitulo(pregunta.getTitulo());
        p.setExplicacion(pregunta.getExplicacion());
        p.setType(pregunta.getType());
        preguntaRepository.save(p);
        JSONArray json = new JSONArray(jsonRespuestas);
        for (int i = 0; i < json.length(); i++) {
            JSONObject rJSON = json.getJSONObject(i);
            Respuesta r = new Respuesta();
            r.setPregunta(p);
            r.setNota(rJSON.getFloat("nota"));
            r.setRespuesta(rJSON.getString("respuesta"));

            respuestaRepository.save(r);

        }

        return "redirect:/cuestionario/" + cuestionario.getId() + "/verpreguntas";
    }

    @PostMapping("crear")
    public String addCuestionario(@ModelAttribute("cuestionario") Cuestionario cuestionario) {
        cuestionario.setAutor((User) session.getAttribute("u"));
        Cuestionario c = cuestionarioService.save(cuestionario);
        return "redirect:/cuestionario/" + c.getId() + "/verpreguntas";
    }

    @GetMapping("crear")
    public String newCuestionario(Model model) {
        model.addAttribute("cuestionario", new Cuestionario());
        return "crearCuestionario";
    }

    @GetMapping("/crear/opciones")
    public String opcionesCreado(Model model) {
        return "opcionesCreado";
    }

    @GetMapping("/{idCuestionario}/{idPregunta}/crearpregunta")
    public String creacionPreguntas(Model model, @PathVariable long idCuestionario, @PathVariable long idPregunta)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        Pregunta pregunta = preguntaRepository.findById(idPregunta).orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        model.addAttribute("pregunta", pregunta);
        return "creacionPreguntas";
    }

    @GetMapping("/{idCuestionario}/{idPregunta}/responder")
    public String responderPreguntas(Model model, @PathVariable long idCuestionario,@PathVariable long idPregunta) throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);

        Pregunta pregunta = preguntaRepository.findById(idPregunta).orElseThrow(() -> new NotFoundException());
        model.addAttribute("pregunta", pregunta);
        return "responderPreguntas";
    }
    @PostMapping("/{idCuestionario}/{idPregunta}/responder")
    public String postResponderPregunta(Pregunta pregunta, @RequestParam String jsonRespuestas,
            @PathVariable Long idCuestionario, @PathVariable Long idPregunta)
            // @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        pregunta.setCuestionario(cuestionario);
        Pregunta siguiente = preguntaRepository.findById(idPregunta + 1)
        .orElseThrow(() -> new NotFoundException());
        if(siguiente.getCuestionario() == cuestionario)
            return "redirect:/cuestionario/" + cuestionario.getId() +"/"+ p.getId()+1 +"/verpreguntas";
        else
            return "redirect:/cuestionario/ranking";

    }
    @GetMapping("/ranking")
    public String ranking(Model model) {
        return "ranking";
    }


    @GetMapping("/{idCuestionario}/verpreguntas")
    public String verPreguntas(Model model, @PathVariable long idCuestionario) throws NotFoundException {

        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        Pregunta pregunta = new Pregunta();
        model.addAttribute("pregunta", pregunta);

        List<Pregunta> preguntas = preguntaRepository.findByCuestionario(cuestionario);
        model.addAttribute("preguntas", preguntas);
        return "verPreguntas";
    }

    @PostMapping("/{idCuestionario}/verpreguntas")
    public String nuevaPregunta(@ModelAttribute("pregunta") Pregunta pregunta, @PathVariable long idCuestionario)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        pregunta.setCuestionario(cuestionario);
        Pregunta p = preguntaRepository.save(pregunta);
        return "redirect:/cuestionario/" + cuestionario.getId() + "/" + p.getId() + "/crearpregunta";

    }

    @PostMapping("/clases")
    public String seleccionClases(@RequestParam("cuestionario-id") String cuestionarioId,
            Model model) {

        return "redirect:/cuestionario/" + cuestionarioId + "/clases";
    }

    @GetMapping("/{idCuestionario}/clases")
    public String obtenerClases(@PathVariable long idCuestionario, Model model) throws NotFoundException {
        List<Clases> clases = claseService.obtenerClases();
        model.addAttribute("clases", clases);
        Clases clase = new Clases();
        model.addAttribute("clase", clase);
        String previousUrl = UriComponentsBuilder.fromPath("/cuestionario/{idCuestionario}/clases")
                .buildAndExpand(idCuestionario)
                .toUriString();
        session.setAttribute("previousUrl", previousUrl);
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());

        model.addAttribute("cuestionario", cuestionario);
        return "seleccionClases";
    }

    @PostMapping("/{idCuestionario}/clases")
    public String procesarClases(@PathVariable long idCuestionario, Model model,
            @RequestParam("clasesSeleccionadas") String clasesSeleccionadas, RedirectAttributes attributes)
            throws NumberFormatException, NotFoundException {

        List<String> checkboxValues = Arrays.asList(clasesSeleccionadas.split(","));
        List<Clases> clases = new ArrayList<>();
        if (checkboxValues != null) {
            for (String checkboxValue : checkboxValues) {
                if (!checkboxValue.isEmpty()) {
                    Clases clase = claseRepository.findById((long) Integer.parseInt(checkboxValue))
                            .orElseThrow(() -> new NotFoundException());
                    clases.add(clase);
                }
            }
        }

        Evento evento = new Evento();
        evento.setClases(clases);
        eventoRepository.save(evento);

        attributes.addAttribute("evento", evento);
        return "redirect:/cuestionario/" + idCuestionario + "/link";

    }

    @PostMapping("/lanzar")
    public String lanzarCuestionario(
            @RequestParam("clasesSeleccionadas") String clasesSeleccionadas,
            @RequestParam("cuestionario-id") String cuestionarioId,
            Model model) {

        // Guardar la ID del cuestionario y la lista de clases en el modelo
        model.addAttribute("cuestionarioId", cuestionarioId);
        model.addAttribute("clasesSeleccionadas", clasesSeleccionadas);

        // Redireccionar a la otra URL después de que la solicitud POST se haya
        // completado
        return "redirect:/cuestionario/" + cuestionarioId + "/link";
    }

    @GetMapping("/{idCuestionario}/link")
    public String lanzarCuestionario(@PathVariable long idCuestionario, Model model,
            @ModelAttribute("evento") Evento evento) throws NotFoundException {
        String code = UserController.generateRandomBase64Token(6);
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());

        evento.setCodigo(code);
        evento.setCuestionario(cuestionario);

        eventoRepository.save(evento);

        model.addAttribute("code", code);
        model.addAttribute("cuestionario", cuestionario);

        return "quizzLink";
    }

    @PostMapping("/{idCuestionario}/link")
    public String lanzarCuestionario(@PathVariable long idCuestionario, Model model)throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        List<Pregunta> pregunta = preguntaRepository.findByCuestionario(cuestionario);
        return "redirect:/cuestionario/" + idCuestionario + "/" +pregunta.get(0).getId() +  "/responder";
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
        File f = localData.getFile("pregunta", "" + id + ".jpg");
        InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : null);
        return os -> FileCopyUtils.copy(in, os);
    }

    /**
     * Uploads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    @PostMapping("{id}/pic")
    @ResponseBody
    public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
            HttpServletResponse response, HttpSession session, Model model) throws IOException, NotFoundException {

        Pregunta target = preguntaRepository.findById(id).orElseThrow(() -> new NotFoundException());
        model.addAttribute("user", target);

        log.info("Updating photo for respuesta {}", id);
        File f = localData.getFile("pregunta", "" + id + ".jpg");
        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
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

    @GetMapping("importar")
    public String vistaImportar(Model model) {

        return "importarCuestionario";
    }

    @PostMapping("importar")
    @ResponseBody
    public String importarCuestionario(@RequestParam("fichero") MultipartFile fichero, Model model)
            throws IOException, NotFoundException {

        log.info("Uploading fichero importacion");
        File f = localData.getFile("importacion", "importacion.xml");
        if (fichero.isEmpty()) {
            log.info("failed to upload file: emtpy file?");
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
                byte[] bytes = fichero.getBytes();
                stream.write(bytes);
                log.info("Uploaded fichero importacion!");
            } catch (Exception e) {
                log.warn("Error uploading ", e);
            }
        }
        Cuestionario cuestionario = new Cuestionario();
        cuestionarioRepository.save(cuestionario);

        List<String> questions = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f);
            NodeList questionNodes = document.getElementsByTagName("question");
            for (int i = 0; i < questionNodes.getLength(); i++) {
                Pregunta pregunta = new Pregunta();
                Element question = (Element) questionNodes.item(i);
                String questionText = question.getElementsByTagName("questiontext").item(0).getTextContent();
                String questionType = question.getAttribute("type").trim();

                questions.add(questionText);
                pregunta.setCuestionario(cuestionario);
                pregunta.setTitulo(questionText.trim());
                switch (questionType) {
                    case "multichoice":
                        pregunta.setType(PreguntaType.OPCION_MULTIPLE);
                        break;
                    case "truefalse":
                        pregunta.setType(PreguntaType.TRUE_FALSE);
                        break;
                    case "shortanswer":
                        pregunta.setType(PreguntaType.RESPUESTA_CORTA);
                        break;
                }

                preguntaRepository.save(pregunta);

                List<String> answerList = new ArrayList<>();
                NodeList answerNodes = question.getElementsByTagName("answer");
                for (int j = 0; j < answerNodes.getLength(); j++) {
                    Element answer = (Element) answerNodes.item(j);
                    String answerText = answer.getTextContent();
                    Integer notaPregunta = Integer.parseInt(answer.getAttribute("fraction"));
                    answerList.add(answerText);

                    Respuesta r = new Respuesta();
                    r.setPregunta(pregunta);
                    r.setNota(notaPregunta / 10);
                    r.setRespuesta(answerText.trim());

                    respuestaRepository.save(r);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"status\":\"Importado correctamente\", \"preguntas\": \"" + questions + "\"}";
    }

}
