
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
@Table(name = "Clases")
public class Clases {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nombre;

	/*
	 * @ManyToMany(mappedBy ="clases" )
	 * private List<User> usuarios = new ArrayList<>();
	 */


}
