package scapp.apischuquiejdev.dto.transportista;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class TransportistaResponse {
    private Long id;
    private PersonaMiniResponse persona;
    private String licenciaNumero;
    private String licenciaTipo;
    private LocalDate licenciaVencimiento;
    private String estado;
    private Boolean aprobado;
    private Boolean activo;
}
