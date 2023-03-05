package es.ucm.fdi.iw.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
@Data
@Table(name = "resultado")
public class Resultado {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private User usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    private Evento evento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respuesta_id")
    private Respuesta respuesta;
}
