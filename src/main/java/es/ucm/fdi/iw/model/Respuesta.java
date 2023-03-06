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
@Table(name="Respuesta")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "id_pregunta")
    private Pregunta pregunta;
    @OneToMany(mappedBy = "respuesta" )
	private List<Resultado> resultados = new ArrayList<>();
    @Column
    private float nota;
    public Respuesta() {
        super();
    }
   
    

}
