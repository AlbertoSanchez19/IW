
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
@Data
@Table(name="Clases")
public class Clases {
    @Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
    @Column
    private String Nombre;

	@ManyToMany(mappedBy ="clases" )
    private List<User> usuarios = new ArrayList<>();

    public Clases() {
		super();
	}
	
	public Clases(Long id, String Nombre, List<User> usuarios) {
		super();
		this.id = id;
		this.Nombre = Nombre;
		this.usuarios = usuarios;
	}
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    public String getNombre() {
		return Nombre;
	}
	public void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}
	public List<User> getUsuarios() {
		return usuarios;
	}
	
	

}


