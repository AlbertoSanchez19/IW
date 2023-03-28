package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name="Pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_cuestionario", nullable = false)
    private Cuestionario cuestionario;
    @Column
    private String titulo;
    @Column
    @Lob
    private String explicacion;
    /* 
    @Column
    private float nota;
    @Column
    private int prioridad;
    */

    @Enumerated(EnumType.STRING)
    private PreguntaType type;

    @OneToMany(mappedBy = "pregunta")
    private List<Respuesta> respuestas = new ArrayList<>();

}

