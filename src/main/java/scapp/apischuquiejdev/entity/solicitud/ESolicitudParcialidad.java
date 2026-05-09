package scapp.apischuquiejdev.entity.solicitud;




import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import scapp.apischuquiejdev.entity.transporte.ETransporte;
import scapp.apischuquiejdev.entity.transportista.ETransportista;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud_parcialidad")
@Getter
@Setter
public class ESolicitudParcialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación de vuelta a la entidad Maestra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private ESolicitud solicitud;

    @Column(name = "numero_viaje", nullable = false)
    private Integer numeroViaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporte_id", nullable = false)
    private ETransporte transporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportista_id", nullable = false)
    private ETransportista transportista;

    @Column(name = "peso_enviado", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoEnviado;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "peso_recibido", precision = 12, scale = 2)
    private BigDecimal pesoRecibido;

    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;

    // Aquí lo dejé como String para coincidir con tu Service ("EN_TRANSITO"),
    // pero si tienes un Enum (ej. EstadoParcialidad) puedes usar @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 40)
    private String estado;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.estado = this.estado == null ? "EN_TRANSITO" : this.estado;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}