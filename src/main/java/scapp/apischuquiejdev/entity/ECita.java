package scapp.apischuquiejdev.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
@Getter
@Setter

public class ECita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "profesional_id", nullable = false)
    private Long profesionalId;

    @Column(name = "servicio_id", nullable = false)
    private Long servicioId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EEstadoCita.EstadoCita estado;

    @Column(name = "notas", length = 1000)
    private String notas;

}
