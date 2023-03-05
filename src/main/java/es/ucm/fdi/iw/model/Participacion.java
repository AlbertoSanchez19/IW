package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Table(name="Participacion")
public class Participacion {

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;
 
    @ManyToOne
    @JoinColumn(name = "id_clase")
    private Clases clase;
}
