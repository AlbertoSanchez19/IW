package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Data
@Table(name="Pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_cuestionario")
    private Cuestionario cuestionario;
    @Column
    private String pregunta;
    @Column
    private String explicacion;
    @Column
    private float nota;
    @Column
    private int prioridad;
    @OneToMany(mappedBy = "pregunta" )
	private List<Respuesta> respuestas = new ArrayList<>();
}
