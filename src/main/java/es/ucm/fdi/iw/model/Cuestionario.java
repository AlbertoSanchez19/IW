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
@Table(name="Cuestionario")
public class Cuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private User autor;
    @Column
    private String descripcion;
    @Column
    private String titulo;

    @OneToMany(mappedBy = "cuestionario")
    private List<Tags> tags = new ArrayList<>();

    @OneToMany(mappedBy = "cuestionario")
    private List<Pregunta> preguntas = new ArrayList<>();
}
