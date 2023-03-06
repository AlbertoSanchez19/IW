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
public class Cuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private List<User> usuario = new ArrayList<>();
    @Column
    private String descripcion;
    @Column
    private String titulo;

    @OneToMany(mappedBy = "cuestionario")
	private List<Tags> tags = new ArrayList<>();

    @OneToMany(mappedBy = "cuestionario" )
	private List<Pregunta> preguntas = new ArrayList<>();
}
