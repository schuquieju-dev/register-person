package scapp.apischuquiejdev.entity.transporte;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import scapp.apischuquiejdev.entity.persona.EPersona;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "transporte",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_transporte_placa", columnNames = {"placa"})
        }
)
@Getter
@Setter
public class ETransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "propietario_persona_id", nullable = false)
    private EPersona propietarioPersona;

    @Column(name = "placa", nullable = false, length = 20)
    private String placa;

    @Column(name = "marca", nullable = false, length = 60)
    private String marca;

    @Column(name = "modelo", length = 60)
    private String modelo;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "color", length = 40)
    private String color;

    @Column(name = "tipo", length = 40)
    private String tipo;

    @Column(name = "capacidad_carga_kg", precision = 12, scale = 2)
    private BigDecimal capacidadCargaKg;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_transporte_id", nullable = false)
    private EEstadoTransporte estadoTransporte;

    @Column(name = "aprobado", nullable = false)
    private Boolean aprobado;

    @Column(name = "aprobado_por")
    private Long aprobadoPor;

    @Column(name = "aprobado_at")
    private OffsetDateTime aprobadoAt;

    @Column(name = "observacion_aprobacion", length = 250)
    private String observacionAprobacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }

        if (this.updatedAt == null) {
            this.updatedAt = now;
        }

        if (this.aprobado == null) {
            this.aprobado = false;
        }

        if (this.activo == null) {
            this.activo = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}