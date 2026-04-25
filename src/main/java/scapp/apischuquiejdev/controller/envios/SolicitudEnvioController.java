package scapp.apischuquiejdev.controller.envios;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioCreateRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.interfaces.services.envios.ISolicitudEnvioService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitud-envios")
@RequiredArgsConstructor
public class SolicitudEnvioController {


    private final ISolicitudEnvioService solicitudEnvioService;

    @PostMapping
    public SolicitudEnvioResponse create(@RequestBody SolicitudEnvioCreateRequest request) {
        return solicitudEnvioService.create(request);
    }

    @PutMapping("/{envioId}/recepcion")
    public SolicitudEnvioResponse registrarRecepcion(
            @PathVariable Long envioId,
            @RequestBody SolicitudEnvioRecepcionRequest request
    ) {
        return solicitudEnvioService.registrarRecepcion(envioId, request);
    }

    @GetMapping("/solicitud/{solicitudId}")
    public List<SolicitudEnvioResponse> findBySolicitudId(@PathVariable Long solicitudId) {
        return solicitudEnvioService.findBySolicitudId(solicitudId);
    }
}
