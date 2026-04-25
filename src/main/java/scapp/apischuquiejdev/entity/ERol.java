package scapp.apischuquiejdev.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rol", schema = "public")
@Getter
@Setter
public class ERol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 50, nullable = false, unique = true)
    private String codigo;


    @Column(name = "nombre", length = 100)
    private String etiquetaRol;



}
