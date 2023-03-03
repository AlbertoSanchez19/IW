package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tag_name;

    @ManyToOne
    @JoinColumn(name = "id_cuestionario")
    private Cuestionario cuestionario;

}
