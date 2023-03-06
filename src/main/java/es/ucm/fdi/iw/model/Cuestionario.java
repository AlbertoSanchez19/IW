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

    @OneToMany(targetEntity = Pregunta.class)
    @JoinColumn(name = "pregunta_id")
    private List<Pregunta> preguntas = new ArrayList<Pregunta>();    

    private String descripcion;
    private String titulo;
}
