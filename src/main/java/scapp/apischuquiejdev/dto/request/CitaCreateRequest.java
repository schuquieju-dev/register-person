package scapp.apischuquiejdev.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import scapp.apischuquiejdev.entity.EEstadoCita;

import java.time.LocalDateTime;

public record CitaCreateRequest (

        @NotNull(message = "clienteId es requerido.")
        Long clienteId,


        @NotNull(message = "profesionalId es requerido.")
        Long profesionalId,

        @NotNull(message = "servicioId es requerido.")
        Long servicioId,

        @NotNull(message = "fechaHora es requerida.")
        @Future(message = "fechaHora debe ser una fecha futura.")
        LocalDateTime fechaHora,

        @NotNull(message = "estado es requerido.")
        EEstadoCita.EstadoCita estado,

        @Size(max = 1000, message = "notas no puede exceder 1000 caracteres.")
        String notas

){
}
