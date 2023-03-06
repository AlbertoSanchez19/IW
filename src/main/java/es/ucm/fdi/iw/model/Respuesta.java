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
    private char opcion;
    @Column 
    private String respuesta;
    @Column
    private float nota;
    public Respuesta() {
        super();
    }
    
    public Respuesta(Pregunta pregunta, char opcion, String respuesta, float nota) {
        super();
        this.pregunta = pregunta;
        this.opcion = opcion;
        this.respuesta = respuesta;
        this.nota = nota;
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public char getOpcion() {
        return opcion;
    }

    public void setOpcion(char opcion) {
        this.opcion = opcion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

}
