package es.ucm.fdi.iw.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.iw.model.*;
import es.ucm.fdi.iw.repository.*;


@Service
public class ProfesorService {
    
    @Autowired
    private ProfesorRepository profesorRepository;

    public Optional<User> obtenerPorId(Long id){
        return profesorRepository.findById(id);
    }
    
    public List<User> obtenerProfesores() {
        return profesorRepository.findByRolesContaining("PROFESOR");
    }

    public User guardarProfesor(User profesor){
        return profesorRepository.save(profesor);
    }

    public List<User> obtenerUsuarios(){
        return profesorRepository.findNotProfesor();
    }
    
}
