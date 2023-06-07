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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
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
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
    private ResultadoRepository resultadoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
    public String responderPreguntas(@RequestParam("code") String code, Model model, @PathVariable long idCuestionario,
            @PathVariable int idPregunta)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        model.addAttribute("idLista", idPregunta);
        Pregunta pregunta = cuestionario.getPreguntas().get(idPregunta);
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("code", code);
        return "responderPreguntas";
    }

    @GetMapping("/{idCuestionario}/{idPregunta}/edicionPreguntas")
    public String editarPregunta(Model model, @PathVariable long idCuestionario, @PathVariable long idPregunta)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        Pregunta pregunta = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        model.addAttribute("pregunta", pregunta);
        return "edicionPreguntas";
    }

    @PostMapping("/{idCuestionario}/{idPregunta}/edicionPreguntas")
    @Transactional
    public String posteditarPregunta(
            @PathVariable long idCuestionario, @PathVariable long idPregunta,
            @RequestParam String titulo,
            @RequestParam String jsonRespuestas) throws NotFoundException {

        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());

        Pregunta pregunta = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new NotFoundException());

        pregunta.setTitulo(titulo);

        preguntaRepository.save(pregunta);

        JSONArray json = new JSONArray(jsonRespuestas);

        respuestaRepository.deleteByPregunta(pregunta);

        for (int i = 0; i < json.length(); i++) {
            JSONObject rJSON = json.getJSONObject(i);
            Respuesta r = new Respuesta();
            r.setPregunta(pregunta);
            r.setNota(rJSON.getFloat("nota"));
            r.setRespuesta(rJSON.getString("respuesta"));

            respuestaRepository.save(r);
        }

        return "redirect:/cuestionario/" + idCuestionario + "/verpreguntas";
    }

    @PostMapping("/{idCuestionario}/{idPregunta}/responder")
    public String postResponderPregunta(Model model, @RequestParam("code") String code,
            @PathVariable Long idCuestionario, @PathVariable int idPregunta, @RequestParam Long id_respuesta,
            @RequestParam String respuesta)
            // @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
            throws NotFoundException {

        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        List<Pregunta> preguntas = cuestionario.getPreguntas();

        Pregunta preguntaActual = preguntas.get(idPregunta);

        // GUARDAR RESULTADO
        if (id_respuesta != -1) {
            Respuesta respuestaSeleccionada = null;
            List<Respuesta> respuestas = preguntaActual.getRespuestas();
            if (preguntaActual.getType() == PreguntaType.RESPUESTA_CORTA
                    || preguntaActual.getType() == PreguntaType.RESPUESTA_FOTO) {
                for (Respuesta r : respuestas) {
                    if (r.getRespuesta().toUpperCase().trim().equals(respuesta.toUpperCase().trim())) {

                        respuestaSeleccionada = r;
                    }

                }
            } else {
                for (Respuesta r : respuestas) {
                    if (r.getId() == id_respuesta) {
                        respuestaSeleccionada = r;
                    }
                }

            }
            if (respuestaSeleccionada != null) {
                boolean yahascontestado = false;
                List<Resultado> resultadosevento = resultadoRepository
                        .findByEvento(eventoRepository.findByCodigo(code));

                for (Resultado result : resultadosevento) {
                    Respuesta respuestaEvent = result.getRespuesta();
                    if (respuestaEvent.getPregunta().equals(preguntaActual)
                            || result.getUsuario().equals(session.getAttribute("u"))) {
                        yahascontestado = true;
                    }
                }
                if (!yahascontestado) {
                    Resultado resultado = new Resultado();
                    resultado.setUsuario((User) session.getAttribute("u"));
                    resultado.setEvento(eventoRepository.findByCodigo(code));
                    resultado.setRespuesta(respuestaSeleccionada);
                    resultadoRepository.save(resultado);
                }

            }
        }

        model.addAttribute("cuestionario", cuestionario);
        model.addAttribute("code", code);

        return "paginaespera";
    }

    @GetMapping("/{idCuestionario}/{idPregunta}/responderProfesor")
    public String responderPreguntasProfesor(@RequestParam("code") String code, Model model,
            @PathVariable long idCuestionario,
            @PathVariable int idPregunta)
            throws NotFoundException {
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        model.addAttribute("cuestionario", cuestionario);
        model.addAttribute("idLista", idPregunta);
        Pregunta pregunta = cuestionario.getPreguntas().get(idPregunta);
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("code", code);
        return "responderProfesor";
    }

    @PostMapping("/{idCuestionario}/{idPregunta}/responderProfesor")
    public String postResponderPreguntaProfesor(@RequestParam("code") String code,
            @PathVariable Long idCuestionario, @PathVariable int idPregunta)
            // @RequestParam("file") MultipartFile file, RedirectAttributes attributes)
            throws NotFoundException {

        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());
        List<Pregunta> preguntas = cuestionario.getPreguntas();

        // SIGUIENTE PREGUNTA SI EXISTE
        idPregunta++;
        if (preguntas.size() > idPregunta) {

            // Genera un código de acceso a la siguiente pregunta; sin este código, no
            // puedes acceder
            String nextCode = UserController.generateRandomBase64Token(5);

            // return "redirect:/cuestionario/" + cuestionario.getId() + "/" + idPregunta +
            // "/responder";
            messagingTemplate.convertAndSend("/topic/" + code,
                    "{ \"type\": \"next\", \"pregunta\": \"" + idPregunta + "\",  \"codigo\": \"" + nextCode + "\"}");
            return "redirect:/cuestionario/" + cuestionario.getId() + "/" + idPregunta + "/responderProfesor?code="
                    + code;

        } else {
            messagingTemplate.convertAndSend("/topic/" + code,
                    "{ \"type\": \"end\"}");
            return "redirect:/cuestionario/ranking?code="
                    + code;
        }

    }

    @GetMapping("/ranking")
    public String ranking(Model model, @RequestParam("code") String code) {
        Evento evento = eventoRepository.findByCodigo(code);
        List<Resultado> resultados = resultadoRepository.findByEvento(evento);
        List<Pregunta> preguntas = evento.getCuestionario().getPreguntas();
        int notaMax = preguntas.size() * 10;

        List<User> usuariosParticipantes = new ArrayList<>();
        List<Integer> notasPorUsuario = new ArrayList<>();
        List<String> nombresParticipantes = new ArrayList<>();
        for (Resultado resultado : resultados) {
            User usuario = resultado.getUsuario();

            // Verifica si el usuario ya existe en la lista de participantes
            if (!usuariosParticipantes.contains(usuario)) {
                usuariosParticipantes.add(usuario);
            }
        }
        for (User usuario : usuariosParticipantes) {
            int nota = 0;
            for (Resultado resultado : resultados) {

                if (resultado.getUsuario().equals(usuario)) {
                    Respuesta respuesta = resultado.getRespuesta();
                    nota += respuesta.getNota();

                }

            }
            nombresParticipantes.add(usuario.getUsername());
            notasPorUsuario.add(nota);
        }

        model.addAttribute("code", code);
        model.addAttribute("participantes", nombresParticipantes);
        model.addAttribute("notas", notasPorUsuario);
        model.addAttribute("notaMax", notaMax);
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
    public String nuevaPregunta(@ModelAttribute("pregunta") Pregunta pregunta, @PathVariable long idCuestionario,
            @RequestParam("code") String code)
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
    public String lanzarCuestionarioPost(@PathVariable long idCuestionario, Model model,
            @RequestParam("code") String code) throws NotFoundException {
        model.addAttribute("code", code);
        Cuestionario cuestionario = cuestionarioRepository.findById(idCuestionario)
                .orElseThrow(() -> new NotFoundException());

        messagingTemplate.convertAndSend("/topic/" + code,
                "{ \"type\": \"start\"}");

        return "redirect:/cuestionario/" + idCuestionario + "/" + 0 + "/responderProfesor?code=" + code;
    }

    /**
     * Downloads a profile pic for a user id
     * 
     * @param id
     * 
     * @return
     * 
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
                pregunta.setType(PreguntaType.byName(questionType));

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

    @PostMapping(value = "/exportar")
    public ResponseEntity<Resource> exportarCuestinario(@RequestParam("cuestionario-id") long cuestionarioId,
            Model model)
            throws NotFoundException {

        Cuestionario cuestionario = cuestionarioRepository.findById(cuestionarioId)
                .orElseThrow(() -> new NotFoundException());

        List<Pregunta> preguntas = cuestionario.getPreguntas();

        String output = "";
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("quiz");
            doc.appendChild(rootElement);

            for (Pregunta pregunta : preguntas) {
                if (pregunta.getType() == PreguntaType.TRUE_FALSE) {
                    List<Respuesta> respuestas = pregunta.getRespuestas();
                    // supercars element
                    Element question = doc.createElement("question");
                    rootElement.appendChild(question);

                    // setting attribute to element
                    Attr attr = doc.createAttribute("type");
                    attr.setValue("truefalse");
                    question.setAttributeNode(attr);

                    // carname element
                    Element enunciado = doc.createElement("questiontext");
                    Attr attrType0 = doc.createAttribute("format");
                    attrType0.setValue("html");
                    enunciado.setAttributeNode(attrType0);

                    Element text = doc.createElement("text");
                    text.appendChild(doc.createTextNode(pregunta.getTitulo()));
                    enunciado.appendChild(text);
                    question.appendChild(enunciado);

                    // carname element
                    Element resp1 = doc.createElement("answer");
                    Attr attrType = doc.createAttribute("fraction");
                    attrType.setValue(Float.toString(respuestas.get(0).getNota() * 10));
                    resp1.setAttributeNode(attrType);

                    Element ans1 = doc.createElement("text");
                    ans1.appendChild(doc.createTextNode(respuestas.get(0).getRespuesta()));

                    resp1.appendChild(ans1);
                    question.appendChild(resp1);

                    Element resp2 = doc.createElement("answer");
                    Attr attrType1 = doc.createAttribute("fraction");
                    attrType1.setValue(Float.toString(respuestas.get(1).getNota() * 10));
                    resp2.setAttributeNode(attrType1);

                    Element ans2 = doc.createElement("text");
                    ans2.appendChild(doc.createTextNode(respuestas.get(1).getRespuesta()));

                    resp2.appendChild(ans2);
                    question.appendChild(resp2);
                }else if (pregunta.getType() == PreguntaType.OPCION_MULTIPLE) {
                    List<Respuesta> respuestas = pregunta.getRespuestas();
                    
                    Element question = doc.createElement("question");
                    rootElement.appendChild(question);

                    
                    Attr attr = doc.createAttribute("type");
                    attr.setValue("multichoice");
                    question.setAttributeNode(attr);

                    
                    Element enunciado = doc.createElement("questiontext");
                    Attr attrType0 = doc.createAttribute("format");
                    attrType0.setValue("html");
                    enunciado.setAttributeNode(attrType0);

                    Element text = doc.createElement("text");
                    text.appendChild(doc.createTextNode(pregunta.getTitulo()));
                    enunciado.appendChild(text);
                    question.appendChild(enunciado);

                    // respuesta 1
                    Element resp1 = doc.createElement("answer");
                    Attr attrType = doc.createAttribute("fraction");
                    attrType.setValue(Float.toString(respuestas.get(0).getNota() * 10));
                    resp1.setAttributeNode(attrType);

                    Element ans1 = doc.createElement("text");
                    ans1.appendChild(doc.createTextNode(respuestas.get(0).getRespuesta()));

                    resp1.appendChild(ans1);
                    question.appendChild(resp1);

                    // respuesta 2

                    Element resp2 = doc.createElement("answer");
                    Attr attrType1 = doc.createAttribute("fraction");
                    attrType1.setValue(Float.toString(respuestas.get(1).getNota() * 10));
                    resp2.setAttributeNode(attrType1);

                    Element ans2 = doc.createElement("text");
                    ans2.appendChild(doc.createTextNode(respuestas.get(1).getRespuesta()));

                    resp2.appendChild(ans2);
                    question.appendChild(resp2);

                    // respuesta 3
                    Element resp3 = doc.createElement("answer");
                    Attr attrType3 = doc.createAttribute("fraction");
                    attrType3.setValue(Float.toString(respuestas.get(2).getNota() * 10));
                    resp3.setAttributeNode(attrType3);

                    Element ans3 = doc.createElement("text");
                    ans3.appendChild(doc.createTextNode(respuestas.get(2).getRespuesta()));

                    resp3.appendChild(ans3);
                    question.appendChild(resp3);

                    // respuesta 4
                    Element resp4 = doc.createElement("answer");
                    Attr attrType4 = doc.createAttribute("fraction");
                    attrType4.setValue(Float.toString(respuestas.get(3).getNota() * 10));
                    resp4.setAttributeNode(attrType4);

                    Element ans4 = doc.createElement("text");
                    ans4.appendChild(doc.createTextNode(respuestas.get(3).getRespuesta()));

                    resp4.appendChild(ans4);
                    question.appendChild(resp4);
                }else if (pregunta.getType() == PreguntaType.RESPUESTA_CORTA || pregunta.getType() == PreguntaType.RESPUESTA_FOTO ) {
                    List<Respuesta> respuestas = pregunta.getRespuestas();
                    
                    Element question = doc.createElement("question");
                    rootElement.appendChild(question);

                    
                    Attr attr = doc.createAttribute("type");
                    attr.setValue("shortanswer");
                    question.setAttributeNode(attr);

                    
                    Element enunciado = doc.createElement("questiontext");
                    Attr attrType0 = doc.createAttribute("format");
                    attrType0.setValue("html");
                    enunciado.setAttributeNode(attrType0);

                    Element text = doc.createElement("text");
                    text.appendChild(doc.createTextNode(pregunta.getTitulo()));
                    enunciado.appendChild(text);
                    question.appendChild(enunciado);

                    // respuesta 1
                    Element resp1 = doc.createElement("answer");
                    Attr attrType = doc.createAttribute("fraction");
                    attrType.setValue(Float.toString(respuestas.get(0).getNota() * 10));
                    resp1.setAttributeNode(attrType);

                    Element ans1 = doc.createElement("text");
                    ans1.appendChild(doc.createTextNode(respuestas.get(0).getRespuesta()));

                    resp1.appendChild(ans1);
                    question.appendChild(resp1);

                    
                }
            }

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            output = writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redireccionar a la otra URL después de que la solicitud POST se haya
        // completado
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(output.getBytes()));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
