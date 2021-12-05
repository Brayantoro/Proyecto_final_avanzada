package co.edu.uniquindio.proyecto.entidades;

import lombok.*;

import javax.persistence.Column;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Ciudad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer codigo;

    @Column(nullable = false, length = 80)
    @ToString.Include
    private String nombre;

    @OneToMany(mappedBy = "ciudadUsuario")
    @ToString.Exclude
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "ciudadProducto")
    @ToString.Exclude
    private List<Producto> productos;



   // public Ciudad(String nombre) {
        //this.nombre = nombre;
    //}
}
