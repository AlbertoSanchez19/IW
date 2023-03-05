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
@Table(name="Evento")
public class Evento {
    @Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
    @JoinColumn(name = "id_clases")
    private Clases clases;
    @Column
    private String Codigo;

    @OneToMany(mappedBy = "evento" )
	private List<Resultado> resultados = new ArrayList<>();
	
    public Evento() {
		super();
	}
	
	public Evento(Long id, String Codigo, Clases clases) {
		super();
		this.id = id;
		this.Codigo = Codigo;
	}
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    public String getCodigo() {
		return Codigo;
	}
	public void setCodigo(String Codigo) {
		this.Codigo = Codigo;
	}
    public Clases getClases() {
		return clases;
	}
	public void setClases(Clases clases) {
		this.clases = clases;
	}
}
