package scapp.apischuquiejdev.controller.envios;





import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.solicitud.SolicitudCompletaRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.interfaces.repository.solicitud.ISolicitudesEnvioService;


import java.util.List;

@RestController
@RequestMapping("/api/solicitudes") // Ruta principal enfocada en la Solicitud Maestra
@RequiredArgsConstructor
public class SolicitudController {



    private final ISolicitudesEnvioService solicitudesEnvioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody SolicitudCompletaRequest request) {

        return solicitudesEnvioService.crearSolicitudConParcialidades(request);
    }


    @PutMapping("/parcialidad/{parcialidadId}/recepcion")
    public SolicitudEnvioResponse registrarRecepcion(
            @PathVariable Long parcialidadId, // Cambiado conceptualmente a parcialidadId
            @RequestBody SolicitudEnvioRecepcionRequest request
    ) {

        return solicitudesEnvioService.registrarRecepcion(parcialidadId, request);
    }


    @GetMapping("/{solicitudId}/parcialidades")
    public List<SolicitudEnvioResponse> findParcialidadesBySolicitudId(@PathVariable Long solicitudId) {

        return solicitudesEnvioService.findBySolicitudId(solicitudId);
    }
}