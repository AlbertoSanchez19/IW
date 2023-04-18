package es.ucm.fdi.iw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.ucm.fdi.iw.model.Clases;
import es.ucm.fdi.iw.repository.ClaseRepository;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    public List<Clases> obtenerClases() {
        return claseRepository.findAll();
    }
}
