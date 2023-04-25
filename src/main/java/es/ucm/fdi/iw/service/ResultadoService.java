package es.ucm.fdi.iw.service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.iw.model.*;
import es.ucm.fdi.iw.repository.*;
public class ResultadoService {
    @Autowired
    private ResultadoRepository resultadoRepository;
  
    public Resultado save(Resultado r) {
		return resultadoRepository.save(r);
	}
}
