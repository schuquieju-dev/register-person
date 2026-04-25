package scapp.apischuquiejdev.entity.solicitud;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import scapp.apischuquiejdev.entity.EEntidad;
import scapp.apischuquiejdev.entity.persona.EPersona;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitud")
@Getter
@Setter
public class ESolicitud {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private EPersona persona;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_id", nullable = false)
    private EEntidad entidad;

    @Column(name = "producto", nullable = false, length = 150)
    private String producto;

    @Column(name = "cantidad_solicitada", precision = 12, scale = 2)
    private BigDecimal cantidadSolicitada;

    @Column(name = "peso_solicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoSolicitado;

    @Column(name = "margen_permitido_porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal margenPermitidoPorcentaje = BigDecimal.ZERO;


    @Column(name = "peso_total_recibido", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoTotalRecibido = BigDecimal.ZERO;

    @Column(name = "peso_pendiente", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoPendiente = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ESolicitudEnvio> envios = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaSolicitud = this.fechaSolicitud == null ? now : this.fechaSolicitud;
        this.createdAt = now;
        this.updatedAt = now;
        this.pesoPendiente = this.pesoSolicitado != null ? this.pesoSolicitado : BigDecimal.ZERO;

        this.pesoTotalRecibido = this.pesoTotalRecibido == null ? BigDecimal.ZERO : this.pesoTotalRecibido;
        this.margenPermitidoPorcentaje = this.margenPermitidoPorcentaje == null ? BigDecimal.ZERO : this.margenPermitidoPorcentaje;
        this.estado = this.estado == null ? EstadoSolicitud.PENDIENTE : this.estado;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
