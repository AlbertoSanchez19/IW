package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="Participacion")
public class Participacion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User usuario;
 
    @ManyToOne
    @JoinColumn(name = "clase_id")
    private Clases clase;

    public void setClase(Clases clase) {
        clase = this.clase;
    }

    public void setUsuario(User attribute) {
        usuario = this.usuario;
    }
}
