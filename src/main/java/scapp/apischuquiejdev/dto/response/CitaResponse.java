package scapp.apischuquiejdev.dto.response;


import scapp.apischuquiejdev.entity.EEstadoCita;

import java.time.LocalDateTime;

public record CitaResponse(
            Long id,
            Long clienteId,
            Long profesionalId,
            Long servicioId,
            LocalDateTime fechaHora,
            EEstadoCita.EstadoCita estado,
            String notas
    ) {
    }

