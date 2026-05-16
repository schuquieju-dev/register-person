package scapp.apischuquiejdev.dto.solicitud;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitudCreadaDto {
    private Long id;
    private String estado;
}