package scapp.apischuquiejdev.entity.transportista;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import scapp.apischuquiejdev.entity.persona.EPersona;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "transportista", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "transportista_persona_id_key", columnNames = {"persona_id"})
        })
@Getter
@Setter
public class ETransportista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false, foreignKey = @ForeignKey(name = "fk_transportista_persona"))
    private EPersona persona;

    @Column(name = "licencia_numero", length = 30)
    private String licenciaNumero;

    @Column(name = "licencia_tipo", length = 20)
    private String licenciaTipo;

    @Column(name = "licencia_vencimiento")
    private LocalDate licenciaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_transportista_id", nullable = false)
    private EEstadoTransportista estadoTransportista;

    @Column(name = "aprobado", nullable = false)
    private Boolean aprobado = false;

    @Column(name = "aprobado_por")
    private Long aprobadoPor;

    @Column(name = "aprobado_at")
    private OffsetDateTime aprobadoAt;

    @Column(name = "observacion_aprobacion", length = 250)
    private String observacionAprobacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.activo == null) {
            this.activo = true;
        }
        if (this.aprobado == null) {
            this.aprobado = false;
        }

    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}