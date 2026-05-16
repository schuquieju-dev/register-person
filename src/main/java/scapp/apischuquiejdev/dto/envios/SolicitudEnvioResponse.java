package scapp.apischuquiejdev.dto.envios;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Builder
public class SolicitudEnvioResponse {


    private Long id;
    private Long solicitudId;
    private Integer numeroEnvio;

    // Datos del Transportista (Persona)
    private Long transportistaId;
    private String transportistaNombre; // Nombre completo concatenado
    private String licenciaNumero;
    private String licenciaTipo;

    // Datos del Transporte (y su Propietario)
    private Long transporteId;
    private String transportePlaca;
    private String transporteMarca;
    private String transporteModelo;
    private String propietarioNombre; // Dueño del transporte



    private LocalDateTime fechaSalida;
    private LocalDateTime fechaRecepcion;
    private BigDecimal pesoEnviado;
    private BigDecimal pesoRecibido;
    private BigDecimal diferenciaPeso;
    private BigDecimal porcentajeDiferencia;
    private String estado;
    private String motivoRechazo;
    private String observacionesEnvio;
    private String observacionesRecepcion;
}
