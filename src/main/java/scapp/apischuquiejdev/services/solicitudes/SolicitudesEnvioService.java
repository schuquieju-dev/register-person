package scapp.apischuquiejdev.services.solicitudes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.dto.solicitud.SolicitudCompletaRequest;
import scapp.apischuquiejdev.dto.solicitud.ViajeRequest;
import scapp.apischuquiejdev.entity.solicitud.ESolicitud;
import scapp.apischuquiejdev.entity.solicitud.ESolicitudParcialidad;
import scapp.apischuquiejdev.entity.solicitud.EstadoSolicitud;

import scapp.apischuquiejdev.interfaces.repository.envios.ISolicitudRepository;
import scapp.apischuquiejdev.interfaces.repository.solicitud.IEntidadRepository;

import scapp.apischuquiejdev.interfaces.repository.solicitud.ISolicitudParcialidadRepository;
import scapp.apischuquiejdev.interfaces.repository.solicitud.ISolicitudesEnvioService;
import scapp.apischuquiejdev.interfaces.repository.transporte.ITransporteRepository;
import scapp.apischuquiejdev.interfaces.repository.transportista.ITransportistaRepository;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudesEnvioService implements ISolicitudesEnvioService {

    private final ISolicitudRepository solicitudRepository;
    private final ISolicitudParcialidadRepository parcialidadRepository; // <- Requerido para actualizar recepciones y buscar viajes
    private final ITransporteRepository transporteRepository;
    private final ITransportistaRepository transportistaRepository;
    private final IEntidadRepository entidadRepository;
    private final IPersonaRepository personaRepository;

    @Override
    @Transactional
    public Long crearSolicitudConParcialidades(SolicitudCompletaRequest request) {

        // 1. VALIDACIÓN DE PESOS
        BigDecimal pesoTotalViajes = request.getParcialidades().stream()
                .map(ViajeRequest::getPesoEnviado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pesoTotalViajes.compareTo(request.getPesoSolicitado()) > 0) {
            throw new IllegalArgumentException("El peso total de los viajes supera el peso solicitado.");
        }

        // 2. CREAR EL MAESTRO (Solicitud)
        ESolicitud solicitud = new ESolicitud();

        // Buscar y setear las relaciones maestras
        solicitud.setPersona(personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada")));
        solicitud.setEntidad(entidadRepository.findById(request.getEntidadId())
                .orElseThrow(() -> new RuntimeException("Entidad no encontrada")));

        solicitud.setProducto(request.getProducto());
        solicitud.setPesoSolicitado(request.getPesoSolicitado());
        solicitud.setPesoTotalEnviado(pesoTotalViajes);
        solicitud.setObservaciones(request.getObservaciones());
        solicitud.setEstado(EstadoSolicitud.EN_PROCESO);

        // 3. CREAR Y ASIGNAR LOS DETALLES (Parcialidades)
        int numeroViaje = 1;
        for (ViajeRequest viajeReq : request.getParcialidades()) {
            ESolicitudParcialidad parcialidad = new ESolicitudParcialidad();

            parcialidad.setNumeroViaje(numeroViaje++);

            // Buscar entidades relacionadas del viaje
            parcialidad.setTransporte(transporteRepository.findById(viajeReq.getTransporteId())
                    .orElseThrow(() -> new RuntimeException("Transporte no encontrado")));
            parcialidad.setTransportista(transportistaRepository.findById(viajeReq.getTransportistaId())
                    .orElseThrow(() -> new RuntimeException("Transportista no encontrado")));

            parcialidad.setPesoEnviado(viajeReq.getPesoEnviado());
            parcialidad.setFechaSalida(viajeReq.getFechaSalida());
            parcialidad.setEstado("EN_TRANSITO");

            // Esto asocia el viaje a la solicitud maestra automáticamente
            solicitud.addParcialidad(parcialidad);
        }

        // 4. GUARDAR TODO DE UNA SOLA VEZ
        ESolicitud solicitudGuardada = solicitudRepository.save(solicitud);

        // Retornamos el ID de la solicitud generada
        return solicitudGuardada.getId();
    }

    @Override
    @Transactional
    public SolicitudEnvioResponse registrarRecepcion(Long parcialidadId, SolicitudEnvioRecepcionRequest request) {

        // 1. Buscar el viaje (parcialidad)
        ESolicitudParcialidad parcialidad = parcialidadRepository.findById(parcialidadId)
                .orElseThrow(() -> new RuntimeException("Viaje/Parcialidad no encontrado"));

        // 2. Actualizar datos de recepción
        parcialidad.setFechaRecepcion(request.getFechaRecepcion() == null ? LocalDateTime.now() : request.getFechaRecepcion());
        parcialidad.setPesoRecibido(request.getPesoRecibido());

        if (request.getObservacionesRecepcion() != null && !request.getObservacionesRecepcion().isEmpty()) {
            String obsAnteriores = parcialidad.getObservaciones() == null ? "" : parcialidad.getObservaciones() + " | ";
            parcialidad.setObservaciones(obsAnteriores + "Recepción: " + request.getObservacionesRecepcion());
        }

        parcialidad.setEstado("RECIBIDA");

        ESolicitudParcialidad saved = parcialidadRepository.save(parcialidad);

        // 3. Actualizar el peso_total_recibido en el Maestro (ESolicitud)
        ESolicitud solicitudMaestra = parcialidad.getSolicitud();

        // Evitamos nulos en caso de que venga null de la BD
        BigDecimal pesoRecibidoActual = solicitudMaestra.getPesoTotalRecibido() != null ? solicitudMaestra.getPesoTotalRecibido() : BigDecimal.ZERO;
        BigDecimal nuevoPesoRecibidoRequest = request.getPesoRecibido() != null ? request.getPesoRecibido() : BigDecimal.ZERO;

        solicitudMaestra.setPesoTotalRecibido(pesoRecibidoActual.add(nuevoPesoRecibidoRequest));
        solicitudRepository.save(solicitudMaestra);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudEnvioResponse> findBySolicitudId(Long solicitudId) {
        return parcialidadRepository.findBySolicitudIdOrderByNumeroViajeAsc(solicitudId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Método Helper para convertir la entidad Parcialidad al DTO de respuesta
    private SolicitudEnvioResponse toResponse(ESolicitudParcialidad parcialidad) {
        return SolicitudEnvioResponse.builder()
                .id(parcialidad.getId())
                .solicitudId(parcialidad.getSolicitud().getId())
                .numeroEnvio(parcialidad.getNumeroViaje())
                .transportistaId(parcialidad.getTransportista().getId())
                .transporteId(parcialidad.getTransporte().getId())
                .fechaSalida(parcialidad.getFechaSalida())
                .fechaRecepcion(parcialidad.getFechaRecepcion())
                .pesoEnviado(parcialidad.getPesoEnviado())
                .pesoRecibido(parcialidad.getPesoRecibido())
                .estado(parcialidad.getEstado())
                .observacionesEnvio(parcialidad.getObservaciones())
                .build();
    }
}