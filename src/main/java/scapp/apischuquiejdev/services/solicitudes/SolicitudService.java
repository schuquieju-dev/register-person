package scapp.apischuquiejdev.services.solicitudes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.envios.SolicitudCreateRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudResponse;
import scapp.apischuquiejdev.entity.EEntidad;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.entity.solicitud.ESolicitud;
import scapp.apischuquiejdev.interfaces.repository.IEntidadRepository;
import scapp.apischuquiejdev.interfaces.repository.envios.ISolicitudRepository;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRepository;
import scapp.apischuquiejdev.interfaces.services.envios.ISolicitudService;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudService implements ISolicitudService {

    private final ISolicitudRepository solicitudRepository;
    private final IPersonaRepository personaRepository;
    private final IEntidadRepository entidadRepository;

    @Override
    public SolicitudResponse create(SolicitudCreateRequest request) {
        EPersona persona = personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        EEntidad entidad = entidadRepository.findById(request.getEntidadId())
                .orElseThrow(() -> new RuntimeException("Entidad no encontrada"));

        ESolicitud solicitud = new ESolicitud();
        solicitud.setPersona(persona);
        solicitud.setEntidad(entidad);
        solicitud.setProducto(request.getProducto());
        solicitud.setCantidadSolicitada(request.getCantidadSolicitada());
        solicitud.setPesoSolicitado(request.getPesoSolicitado());
        solicitud.setMargenPermitidoPorcentaje(
                request.getMargenPermitidoPorcentaje() == null ? BigDecimal.ZERO : request.getMargenPermitidoPorcentaje()
        );

        solicitud.setPesoTotalRecibido(BigDecimal.ZERO);
        solicitud.setPesoPendiente(
                request.getPesoSolicitado() == null ? BigDecimal.ZERO : request.getPesoSolicitado()
        );
        solicitud.setObservaciones(request.getObservaciones());

        return toResponse(solicitudRepository.save(solicitud));
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponse findById(Long id) {
        ESolicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        return toResponse(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponse> findAll() {
        return solicitudRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SolicitudResponse toResponse(ESolicitud solicitud) {
        return SolicitudResponse.builder()
                .id(solicitud.getId())
                .personaId(solicitud.getPersona().getId())
                .entidadId(solicitud.getEntidad().getId())
                .producto(solicitud.getProducto())
                .cantidadSolicitada(solicitud.getCantidadSolicitada())
                .pesoSolicitado(solicitud.getPesoSolicitado())
                .margenPermitidoPorcentaje(solicitud.getMargenPermitidoPorcentaje())

                .pesoTotalRecibido(solicitud.getPesoTotalRecibido())
                .pesoPendiente(solicitud.getPesoPendiente())
                .estado(solicitud.getEstado().name())
                .observaciones(solicitud.getObservaciones())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .build();
    }
}

