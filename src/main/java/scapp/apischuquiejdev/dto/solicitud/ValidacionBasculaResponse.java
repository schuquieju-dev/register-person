package scapp.apischuquiejdev.dto.solicitud;


import lombok.Builder;
import lombok.Data;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;

@Data
@Builder
public class ValidacionBasculaResponse {
    private Long solicitudId;
    private String clienteNombre;
    private String productoNombre;
    private String observacionesSolicitud;

    // Datos para que el operador compare
    private SolicitudEnvioResponse detalleEnvio;
}