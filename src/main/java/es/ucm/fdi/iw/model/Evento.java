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
@Table(name = "Evento")
public class Evento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "evento_id")
	private List<Clases> clases = new ArrayList<>();
	@Column
	private String codigo;

	@OneToMany(mappedBy = "evento")
	private List<Resultado> resultados = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cuestionario_id")
	private Cuestionario cuestionario;

	public Evento() {
		super();
	}

	public Evento(Long id, String codigo, List<Clases> clases) {
		super();
		this.id = id;
		this.codigo = codigo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<Clases> getClases() {
		return clases;
	}

	public void setClases(List<Clases> clases) {
		this.clases = clases;
	}
}
