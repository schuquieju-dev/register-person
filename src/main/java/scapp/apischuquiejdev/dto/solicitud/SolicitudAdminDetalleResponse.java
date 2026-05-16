package scapp.apischuquiejdev.dto.solicitud;


import lombok.Builder;
import lombok.Data;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class SolicitudAdminDetalleResponse {
    // Datos del Encabezado (Maestro)
    private Long id;
    private String clienteNombre; // De Persona
    private String entidadNombre; // De Entidad
    private String productoNombre; // De ProductoCatalogo
    private BigDecimal pesoSolicitado;
    private BigDecimal pesoTotalEnviado;
    private BigDecimal pesoTotalRecibido;
    private String estado;
    private String observaciones;

    // Lista de Parcialidades (Detalle)
    private List<SolicitudEnvioResponse> parcialidades;


    private BigDecimal diferenciaPeso; // El valor absoluto de la diferencia
    private BigDecimal porcentajeDesviacion;
    private String tipoBalance; // "EXCEDENTE", "FALTANTE" o "EXACTO"


    private BigDecimal precioUnitarioPactado;


    private BigDecimal margenPermitido;
    private String alertaMargen; // "DENTRO" o "FUERA"




}