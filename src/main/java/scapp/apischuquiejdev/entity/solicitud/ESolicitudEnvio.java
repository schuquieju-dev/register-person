package scapp.apischuquiejdev.entity.solicitud;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import scapp.apischuquiejdev.entity.transporte.ETransporte;
import scapp.apischuquiejdev.entity.transportista.ETransportista;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "solicitud_envio",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_solicitud_envio_numero", columnNames = {"solicitud_id", "numero_envio"})
        }
)
@Getter
@Setter
public class ESolicitudEnvio {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private ESolicitud solicitud;

    @Column(name = "numero_envio", nullable = false)
    private Integer numeroEnvio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "transportista_id", nullable = false)
    private ETransportista transportista;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "transporte_id", nullable = false)
    private ETransporte transporte;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;

    @Column(name = "cantidad_enviada", precision = 12, scale = 2)
    private BigDecimal cantidadEnviada;

    @Column(name = "peso_enviado", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoEnviado;

    @Column(name = "cantidad_recibida", precision = 12, scale = 2)
    private BigDecimal cantidadRecibida;

    @Column(name = "peso_recibido", precision = 12, scale = 2)
    private BigDecimal pesoRecibido;

    @Column(name = "diferencia_peso", nullable = false, precision = 12, scale = 2)
    private BigDecimal diferenciaPeso = BigDecimal.ZERO;

    @Column(name = "porcentaje_diferencia", nullable = false, precision = 7, scale = 2)
    private BigDecimal porcentajeDiferencia = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 40)
    private EstadoSolicitudEnvio estado = EstadoSolicitudEnvio.PENDIENTE;

    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;

    @Column(name = "observaciones_envio", columnDefinition = "TEXT")
    private String observacionesEnvio;

    @Column(name = "observaciones_recepcion", columnDefinition = "TEXT")
    private String observacionesRecepcion;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.estado = this.estado == null ? EstadoSolicitudEnvio.PENDIENTE : this.estado;
        this.diferenciaPeso = this.diferenciaPeso == null ? BigDecimal.ZERO : this.diferenciaPeso;
        this.porcentajeDiferencia = this.porcentajeDiferencia == null ? BigDecimal.ZERO : this.porcentajeDiferencia;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
