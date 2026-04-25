package scapp.apischuquiejdev.controller.envios;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scapp.apischuquiejdev.dto.envios.SolicitudCreateRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudResponse;
import scapp.apischuquiejdev.interfaces.services.envios.ISolicitudService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {


    private final ISolicitudService solicitudService;

    @PostMapping
    public SolicitudResponse create(@RequestBody SolicitudCreateRequest request) {
        return solicitudService.create(request);
    }

    @GetMapping("/{id}")
    public SolicitudResponse findById(@PathVariable Long id) {
        return solicitudService.findById(id);
    }

    @GetMapping
    public List<SolicitudResponse> findAll() {
        return solicitudService.findAll();
    }

}
