package scapp.apischuquiejdev.entity.solicitud;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;




import java.time.LocalDateTime;

@Entity
@Table(name = "producto_catalogo")
@Getter
@Setter
public class EProductoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "variedad", length = 50)
    private String variedad;

    @Column(name = "proceso", length = 50)
    private String proceso;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado_activo", nullable = false)
    private Boolean estadoActivo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.estadoActivo == null) {
            this.estadoActivo = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}