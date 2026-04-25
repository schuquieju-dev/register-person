package scapp.apischuquiejdev.dto.transportista;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import scapp.apischuquiejdev.dto.persona.PersonaCreateRequest;

import java.time.LocalDate;

@Data
public class TransportistaCreateRequest {
    private Long personaId;

    @Valid
    private PersonaCreateRequest persona; // requerido si personaId == null

    @Size(max = 30)
    private String licenciaNumero;

    @Size(max = 20)
    private String licenciaTipo;

    private LocalDate licenciaVencimiento;
}