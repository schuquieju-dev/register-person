package scapp.apischuquiejdev.entity.solicitud;

import lombok.Builder;
import lombok.Data;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;

@Data
@Builder
public class ValidacionGaritaResponse {
    // Info del Maestro para contexto
    private Long solicitudId;
    private String clienteNombre;
    private String productoNombre;
    private String entidadDestino;

    // Info de la Parcialidad para validar físicamente
    private SolicitudEnvioResponse detalleEnvio;
}