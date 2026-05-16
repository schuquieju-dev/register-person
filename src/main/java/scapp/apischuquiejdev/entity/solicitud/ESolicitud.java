package scapp.apischuquiejdev.entity.solicitud;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import scapp.apischuquiejdev.entity.persona.EPersona;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    // Relación al catálogo de productos (Cafés)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private EProductoCatalogo producto;

    @Column(name = "precio_unitario_pactado", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitarioPactado;

    @Column(name = "cantidad_solicitada", precision = 12, scale = 2)
    private BigDecimal cantidadSolicitada;

    @Column(name = "peso_solicitado", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoSolicitado;

    @Column(name = "peso_total_enviado", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoTotalEnviado = BigDecimal.ZERO;

    @Column(name = "margen_permitido_porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal margenPermitidoPorcentaje = BigDecimal.ZERO;



    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ESolicitudParcialidad> parcialidades = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;




    @Column(name = "peso_total_recibido", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoTotalRecibido = BigDecimal.ZERO;

    @Column(name = "peso_pendiente", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoPendiente = BigDecimal.ZERO;

    // --- NUEVOS CAMPOS PARA LIQUIDACIÓN ---

    @Column(name = "diferencia_peso", precision = 12, scale = 2)
    private BigDecimal diferenciaPeso = BigDecimal.ZERO; // (Recibido - Solicitado)

    @Column(name = "porcentaje_desviacion", precision = 5, scale = 2)
    private BigDecimal porcentajeDesviacion = BigDecimal.ZERO;

    // ... (enums y fechas se mantienen igual) ...

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaSolicitud = this.fechaSolicitud == null ? now : this.fechaSolicitud;
        this.createdAt = now;
        this.updatedAt = now;

        this.pesoPendiente = this.pesoSolicitado != null ? this.pesoSolicitado : BigDecimal.ZERO;
        this.pesoTotalEnviado = this.pesoTotalEnviado == null ? BigDecimal.ZERO : this.pesoTotalEnviado;
        this.pesoTotalRecibido = this.pesoTotalRecibido == null ? BigDecimal.ZERO : this.pesoTotalRecibido;
        this.margenPermitidoPorcentaje = this.margenPermitidoPorcentaje == null ? BigDecimal.ZERO : this.margenPermitidoPorcentaje;
        this.estado = (this.estado == null) ? EstadoSolicitud.PENDIENTE : this.estado;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        // Cada vez que se actualiza, recalculamos el pendiente
        recalcularBalances();
    }

    /**
     * Método interno para calcular cuánto falta, excedentes y desviaciones.
     */
    public void recalcularBalances() {
        if (this.pesoSolicitado != null && this.pesoSolicitado.compareTo(BigDecimal.ZERO) > 0) {
            // 1. Peso Pendiente: Solo tiene sentido si no hemos recibido más de lo solicitado
            BigDecimal pendiente = this.pesoSolicitado.subtract(this.pesoTotalRecibido);
            this.pesoPendiente = pendiente.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : pendiente;

            // 2. Diferencia Real (Para liquidación)
            this.diferenciaPeso = this.pesoTotalRecibido.subtract(this.pesoSolicitado);

            // 3. Porcentaje de Desviación
            this.porcentajeDesviacion = this.diferenciaPeso
                    .divide(this.pesoSolicitado, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    public void addParcialidad(ESolicitudParcialidad parcialidad) {
        parcialidades.add(parcialidad);
        parcialidad.setSolicitud(this);
    }
}