package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import es.ucm.fdi.iw.service.ClaseService;
import es.ucm.fdi.iw.model.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @GetMapping("/obtener_clases")
    public List<String> obtenerClases() {
        List<Clases> clases = claseService.obtenerClases();
        return clases.stream()
                .map(Clases::getNombre)
                .collect(Collectors.toList());
    }
}
