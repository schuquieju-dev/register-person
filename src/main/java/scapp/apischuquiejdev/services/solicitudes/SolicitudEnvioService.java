package scapp.apischuquiejdev.services.solicitudes;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioCreateRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.entity.solicitud.ESolicitud;
import scapp.apischuquiejdev.entity.solicitud.ESolicitudEnvio;
import scapp.apischuquiejdev.entity.solicitud.EstadoSolicitud;
import scapp.apischuquiejdev.entity.solicitud.EstadoSolicitudEnvio;
import scapp.apischuquiejdev.entity.transporte.ETransporte;
import scapp.apischuquiejdev.entity.transportista.ETransportista;
import scapp.apischuquiejdev.interfaces.repository.envios.ISolicitudEnvioRepository;
import scapp.apischuquiejdev.interfaces.repository.envios.ISolicitudRepository;
import scapp.apischuquiejdev.interfaces.repository.transporte.ITransporteRepository;
import scapp.apischuquiejdev.interfaces.repository.transportista.ITransportistaRepository;
import scapp.apischuquiejdev.interfaces.services.envios.ISolicitudEnvioService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudEnvioService implements ISolicitudEnvioService {

    private final ISolicitudEnvioRepository solicitudEnvioRepository;
    private final ISolicitudRepository solicitudRepository;
    private final ITransportistaRepository transportistaRepository;
    private final ITransporteRepository transporteRepository;

    @Override
    public SolicitudEnvioResponse create(SolicitudEnvioCreateRequest  request) {
        ESolicitud solicitud = solicitudRepository.findById(request.getSolicitudId())
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() == EstadoSolicitud.CANCELADA || solicitud.getEstado() == EstadoSolicitud.COMPLETADA) {
            throw new RuntimeException("La solicitud no permite nuevos envios");
        }

        ETransportista transportista = transportistaRepository.findById(request.getTransportistaId())
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        ETransporte transporte = transporteRepository.findById(request.getTransporteId())
                .orElseThrow(() -> new RuntimeException("Transporte no encontrado"));

        Integer numeroEnvio = obtenerSiguienteNumeroEnvio(solicitud.getId());

        ESolicitudEnvio envio = new ESolicitudEnvio();
        envio.setSolicitud(solicitud);
        envio.setNumeroEnvio(numeroEnvio);
        envio.setTransportista(transportista);
        envio.setTransporte(transporte);
        envio.setFechaSalida(request.getFechaSalida() == null ? LocalDateTime.now() : request.getFechaSalida());
        envio.setCantidadEnviada(request.getCantidadEnviada());
        envio.setPesoEnviado(request.getPesoEnviado());
        envio.setEstado(EstadoSolicitudEnvio.EN_TRANSITO);
        envio.setObservacionesEnvio(request.getObservacionesEnvio());

        ESolicitudEnvio saved = solicitudEnvioRepository.save(envio);

        recalcularTotalesSolicitud(solicitud);

        return toResponse(saved);
    }

    @Override
    public SolicitudEnvioResponse registrarRecepcion(Long envioId, SolicitudEnvioRecepcionRequest request) {
        ESolicitudEnvio envio = solicitudEnvioRepository.findById(envioId)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));

        envio.setFechaRecepcion(request.getFechaRecepcion() == null ? LocalDateTime.now() : request.getFechaRecepcion());
        envio.setCantidadRecibida(request.getCantidadRecibida());
        envio.setPesoRecibido(request.getPesoRecibido());
        envio.setObservacionesRecepcion(request.getObservacionesRecepcion());

        BigDecimal pesoEnviado = nvl(envio.getPesoEnviado());
        BigDecimal pesoRecibido = nvl(envio.getPesoRecibido());
        BigDecimal diferencia = pesoEnviado.subtract(pesoRecibido).abs();

        envio.setDiferenciaPeso(diferencia);

        BigDecimal porcentaje = BigDecimal.ZERO;
        if (pesoEnviado.compareTo(BigDecimal.ZERO) > 0) {
            porcentaje = diferencia
                    .multiply(BigDecimal.valueOf(100))
                    .divide(pesoEnviado, 2, RoundingMode.HALF_UP);
        }

        envio.setPorcentajeDiferencia(porcentaje);

        BigDecimal margen = nvl(envio.getSolicitud().getMargenPermitidoPorcentaje());

        if (porcentaje.compareTo(margen) > 0) {
            envio.setEstado(EstadoSolicitudEnvio.RECHAZADO);
            envio.setMotivoRechazo("Excede margen permitido de " + margen + "%");
        } else if (porcentaje.compareTo(BigDecimal.ZERO) > 0) {
            envio.setEstado(EstadoSolicitudEnvio.RECIBIDO_CON_OBSERVACION);
            envio.setMotivoRechazo(null);
        } else {
            envio.setEstado(EstadoSolicitudEnvio.RECIBIDO);
            envio.setMotivoRechazo(null);
        }

        ESolicitudEnvio saved = solicitudEnvioRepository.save(envio);

        recalcularTotalesSolicitud(envio.getSolicitud());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudEnvioResponse> findBySolicitudId(Long solicitudId) {
        return solicitudEnvioRepository.findBySolicitudIdOrderByNumeroEnvioAsc(solicitudId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Integer obtenerSiguienteNumeroEnvio(Long solicitudId) {
        Optional<ESolicitudEnvio> ultimo = solicitudEnvioRepository.findTopBySolicitudIdOrderByNumeroEnvioDesc(solicitudId);
        return ultimo.map(solicitudEnvio -> solicitudEnvio.getNumeroEnvio() + 1).orElse(1);
    }

    private void recalcularTotalesSolicitud(ESolicitud solicitud) {
        List<ESolicitudEnvio> envios = solicitudEnvioRepository.findBySolicitudIdOrderByNumeroEnvioAsc(solicitud.getId());

        BigDecimal totalEnviado = BigDecimal.ZERO;
        BigDecimal totalRecibido = BigDecimal.ZERO;

        for (ESolicitudEnvio envio : envios) {
            if (envio.getEstado() != EstadoSolicitudEnvio.CANCELADO) {
                totalEnviado = totalEnviado.add(nvl(envio.getPesoEnviado()));
            }

            if (envio.getEstado() == EstadoSolicitudEnvio.RECIBIDO
                    || envio.getEstado() == EstadoSolicitudEnvio.RECIBIDO_CON_OBSERVACION) {
                totalRecibido = totalRecibido.add(nvl(envio.getPesoRecibido()));
            }
        }

        solicitud.setPesoTotalRecibido(totalRecibido);

        BigDecimal pendiente = nvl(solicitud.getPesoSolicitado()).subtract(totalRecibido);
        if (pendiente.compareTo(BigDecimal.ZERO) < 0) {
            pendiente = BigDecimal.ZERO;
        }

        solicitud.setPesoPendiente(pendiente);

        if (totalRecibido.compareTo(BigDecimal.ZERO) <= 0) {
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        } else if (pendiente.compareTo(BigDecimal.ZERO) > 0) {
            solicitud.setEstado(EstadoSolicitud.EN_PROCESO);
        } else {
            solicitud.setEstado(EstadoSolicitud.COMPLETADA);
        }

        solicitudRepository.save(solicitud);
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private SolicitudEnvioResponse toResponse(ESolicitudEnvio envio) {
        return SolicitudEnvioResponse.builder()
                .id(envio.getId())
                .solicitudId(envio.getSolicitud().getId())
                .numeroEnvio(envio.getNumeroEnvio())
                .transportistaId(envio.getTransportista().getId())
                .transporteId(envio.getTransporte().getId())
                .fechaSalida(envio.getFechaSalida())
                .fechaRecepcion(envio.getFechaRecepcion())
                .cantidadEnviada(envio.getCantidadEnviada())
                .pesoEnviado(envio.getPesoEnviado())
                .cantidadRecibida(envio.getCantidadRecibida())
                .pesoRecibido(envio.getPesoRecibido())
                .diferenciaPeso(envio.getDiferenciaPeso())
                .porcentajeDiferencia(envio.getPorcentajeDiferencia())
                .estado(envio.getEstado().name())
                .motivoRechazo(envio.getMotivoRechazo())
                .observacionesEnvio(envio.getObservacionesEnvio())
                .observacionesRecepcion(envio.getObservacionesRecepcion())
                .build();
    }
}

