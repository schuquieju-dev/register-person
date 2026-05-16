package scapp.apischuquiejdev.services.solicitudes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioRecepcionRequest;
import scapp.apischuquiejdev.dto.envios.SolicitudEnvioResponse;
import scapp.apischuquiejdev.dto.solicitud.*;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.entity.solicitud.ESolicitud;
import scapp.apischuquiejdev.entity.solicitud.ESolicitudParcialidad;
import scapp.apischuquiejdev.entity.solicitud.EstadoSolicitud;
import scapp.apischuquiejdev.entity.solicitud.ValidacionGaritaResponse;
import scapp.apischuquiejdev.interfaces.repository.IProductoCatalogoRepository;
import scapp.apischuquiejdev.interfaces.repository.envios.ISolicitudRepository;
import scapp.apischuquiejdev.interfaces.repository.solicitud.IEntidadRepository;
import scapp.apischuquiejdev.interfaces.repository.solicitud.ISolicitudParcialidadRepository;
import scapp.apischuquiejdev.interfaces.repository.solicitud.ISolicitudesEnvioService;
import scapp.apischuquiejdev.interfaces.repository.transporte.ITransporteRepository;
import scapp.apischuquiejdev.interfaces.repository.transportista.ITransportistaRepository;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRepository;
import scapp.apischuquiejdev.util.BusinessException;
import scapp.apischuquiejdev.util.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudesEnvioService implements ISolicitudesEnvioService {

    private final ISolicitudRepository solicitudRepository;
    private final ISolicitudParcialidadRepository parcialidadRepository;
    private final ITransporteRepository transporteRepository;
    private final ITransportistaRepository transportistaRepository;
    private final IEntidadRepository entidadRepository;
    private final IPersonaRepository personaRepository;
    private final IProductoCatalogoRepository productoRepository;

    @Override
    @Transactional
    public SolicitudCreadaDto crearSolicitudConParcialidades(SolicitudCompletaRequest request) {
        BigDecimal pesoTotalViajes = request.getParcialidades().stream()
                .map(ViajeRequest::getPesoEnviado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (pesoTotalViajes.compareTo(request.getPesoSolicitado()) > 0) {
            throw new BusinessException("El peso total de los viajes supera el peso solicitado.");
        }

        ESolicitud solicitud = new ESolicitud();
        solicitud.setPersona(personaRepository.findById(request.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con ID: " + request.getPersonaId())));

        solicitud.setEntidad(entidadRepository.findById(request.getEntidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrada")));

        solicitud.setProducto(productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto del catálogo no encontrado")));

        solicitud.setPrecioUnitarioPactado(request.getPrecioUnitarioPactado());
        solicitud.setPesoSolicitado(request.getPesoSolicitado());
        solicitud.setMargenPermitidoPorcentaje(request.getMargenPermitidoPorcentaje());
        solicitud.setPesoTotalEnviado(pesoTotalViajes);
        solicitud.setObservaciones(request.getObservaciones());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        int numeroViaje = 1;
        for (ViajeRequest viajeReq : request.getParcialidades()) {
            ESolicitudParcialidad parcialidad = new ESolicitudParcialidad();
            parcialidad.setNumeroViaje(numeroViaje++);
            parcialidad.setTransporte(transporteRepository.findById(viajeReq.getTransporteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Transporte no encontrado")));
            parcialidad.setTransportista(transportistaRepository.findById(viajeReq.getTransportistaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Transportista no encontrado")));

            parcialidad.setPesoEnviado(viajeReq.getPesoEnviado());
            parcialidad.setFechaSalida(viajeReq.getFechaSalida());
            parcialidad.setEstado("EN_TRANSITO");
            solicitud.addParcialidad(parcialidad);
        }

        ESolicitud solicitudGuardada = solicitudRepository.save(solicitud);

        return SolicitudCreadaDto.builder()
                .id(solicitudGuardada.getId())
                .estado(solicitudGuardada.getEstado().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudEnvioResponse> findBySolicitudId(Long solicitudId) {
        return parcialidadRepository.findBySolicitudIdOrderByNumeroViajeAsc(solicitudId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SolicitudAdminDetalleResponse actualizarEstado(Long solicitudId, UpdateEstadoRequest request) {
        ESolicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada para actualizar."));

        if (solicitud.getEstado() == EstadoSolicitud.LIQUIDADA) {
            throw new BusinessException("No se puede cambiar el estado de una solicitud ya FINALIZADA.");
        }

        solicitud.setEstado(request.getNuevoEstado());
        if (request.getObservacionAdmin() != null) {
            String obsActual = solicitud.getObservaciones() != null ? solicitud.getObservaciones() : "";
            solicitud.setObservaciones(obsActual + " | Admin: " + request.getObservacionAdmin());
        }

        ESolicitud guardada = solicitudRepository.save(solicitud);
        return mapToAdminDetalle(guardada);
    }

    // LISTADO LIGERO (Para la tabla principal)
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudAdminResumenResponse> obtenerResumenParaAdmin() {
        return solicitudRepository.findAll().stream()
                .map(s -> {
                    s.recalcularBalances();
                    return SolicitudAdminResumenResponse.builder()
                            .id(s.getId())
                            .clienteNombre(formatearNombreCompleto(s.getPersona()))
                            .productoNombre(s.getProducto().getNombre())
                            .pesoSolicitado(s.getPesoSolicitado())
                            .pesoTotalRecibido(s.getPesoTotalRecibido())
                            .porcentajeDesviacion(s.getPorcentajeDesviacion())
                            .estado(s.getEstado().name())
                            .fechaSolicitud(s.getFechaSolicitud())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // DETALLE PROFUNDO (Para el botón "Revisar Detalles")
    @Override
    @Transactional(readOnly = true)
    public SolicitudAdminDetalleResponse obtenerDetalleAdmin(Long solicitudId) {
        ESolicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        return mapToAdminDetalle(solicitud);
    }

    @Override
    @Transactional
    public SolicitudEnvioResponse registrarIngresoGarita(Long parcialidadId, GaritaRequest request) {
        ESolicitudParcialidad parcialidad = parcialidadRepository.findById(parcialidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + parcialidadId));

        if ("RECIBIDA".equals(parcialidad.getEstado())) {
            throw new BusinessException("Acceso denegado: El viaje ya fue procesado en báscula y no puede ser modificado.");
        }

        if ("RECHAZADA".equals(parcialidad.getEstado())) {
            throw new BusinessException("Este viaje ya fue rechazado anteriormente. Debe generar un nuevo envío.");
        }

        String obsActuales = parcialidad.getObservaciones() != null ? parcialidad.getObservaciones() : "";
        String obsNueva = (request.getObservaciones() != null && !request.getObservaciones().isEmpty())
                ? " - " + request.getObservaciones()
                : "";

        if (request.isAprobado()) {
            parcialidad.setEstado("EN_GARITA");
            parcialidad.setObservaciones(obsActuales + " | Garita: Aprobado" + obsNueva + ".");
        } else {
            parcialidad.setEstado("RECHAZADA");
            parcialidad.setObservaciones(obsActuales + " | Garita: RECHAZADO" + obsNueva + ".");
        }

        return toResponse(parcialidadRepository.save(parcialidad));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidacionGaritaResponse obtenerDetalleParaValidacion(Long solicitudId, Long parcialidadId) {
        ESolicitudParcialidad parcialidad = parcialidadRepository.findById(parcialidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado con ID: " + parcialidadId));

        if (!parcialidad.getSolicitud().getId().equals(solicitudId)) {
            throw new BusinessException("La parcialidad no corresponde a la solicitud proporcionada.");
        }

        ESolicitud solicitud = parcialidad.getSolicitud();

        return ValidacionGaritaResponse.builder()
                .solicitudId(solicitud.getId())
                .clienteNombre(formatearNombreCompleto(solicitud.getPersona()))
                .productoNombre(solicitud.getProducto().getNombre())
                .entidadDestino(solicitud.getEntidad().getNombre())
                .detalleEnvio(toResponse(parcialidad))
                .build();
    }

    @Override
    @Transactional
    public SolicitudEnvioResponse registrarRecepcion(Long parcialidadId, SolicitudEnvioRecepcionRequest request) {
        ESolicitudParcialidad parcialidad = parcialidadRepository.findById(parcialidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado para pesaje."));

        if (!"EN_GARITA".equals(parcialidad.getEstado())) {
            throw new BusinessException("El vehículo debe estar EN_GARITA para registrar pesaje.");
        }

        parcialidad.setPesoRecibido(request.getPesoRecibido());
        parcialidad.setFechaRecepcion(LocalDateTime.now());
        parcialidad.setEstado("RECIBIDA");

        if (request.getObservacionesRecepcion() != null && !request.getObservacionesRecepcion().isBlank()) {
            String obs = parcialidad.getObservaciones() != null ? parcialidad.getObservaciones() : "";
            parcialidad.setObservaciones(obs + " | Báscula: " + request.getObservacionesRecepcion());
        }

        ESolicitud solicitud = parcialidad.getSolicitud();

        long parcialidadesPendientes = solicitud.getParcialidades().stream()
                .filter(p -> !p.getId().equals(parcialidadId))
                .filter(p -> !"RECIBIDA".equals(p.getEstado()) && !"RECHAZADA".equals(p.getEstado()))
                .count();

        if (parcialidadesPendientes == 0) {
            solicitud.setEstado(EstadoSolicitud.RECIBIDA);
            String cierreMsg = " | Sistema: Cierre físico detectado (Última parcialidad).";
            solicitud.setObservaciones(solicitud.getObservaciones() + cierreMsg);
        } else {
            solicitud.setEstado(EstadoSolicitud.EN_RECEPCION);
        }

        BigDecimal actual = solicitud.getPesoTotalRecibido() != null ? solicitud.getPesoTotalRecibido() : BigDecimal.ZERO;
        solicitud.setPesoTotalRecibido(actual.add(request.getPesoRecibido()));

        solicitudRepository.save(solicitud);
        return toResponse(parcialidadRepository.save(parcialidad));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidacionBasculaResponse obtenerDetalleParaBascula(Long solicitudId, Long parcialidadId) {
        ESolicitudParcialidad parcialidad = parcialidadRepository.findById(parcialidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Viaje no encontrado."));

        if (!"EN_GARITA".equals(parcialidad.getEstado())) {
            throw new BusinessException("El vehículo con placa " + parcialidad.getTransporte().getPlaca() +
                    " no ha registrado ingreso en garita.");
        }

        ESolicitud solicitud = parcialidad.getSolicitud();

        return ValidacionBasculaResponse.builder()
                .solicitudId(solicitud.getId())
                .clienteNombre(formatearNombreCompleto(solicitud.getPersona()))
                .productoNombre(solicitud.getProducto().getNombre())
                .observacionesSolicitud(solicitud.getObservaciones())
                .detalleEnvio(toResponse(parcialidad))
                .build();
    }



    @Override
    @Transactional
    public SolicitudAdminDetalleResponse liquidarSolicitud(Long solicitudId) {
        ESolicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        // Solo se puede liquidar si ya se terminó de recibir físicamente el café
        if (solicitud.getEstado() != EstadoSolicitud.RECIBIDA) {
            throw new BusinessException("La solicitud debe estar en estado RECIBIDA para poder liquidarse.");
        }

        // 1. Forzar recalculo de balances (Diferencia, Porcentajes, etc.)
        solicitud.recalcularBalances();

        BigDecimal margenPermitido = solicitud.getMargenPermitidoPorcentaje();
        BigDecimal desviacionReal = solicitud.getPorcentajeDesviacion(); // Ej: -2.5 o 5.0
        BigDecimal desviacionAbsoluta = desviacionReal.abs();

        // 2. Determinar el estado final según la desviación y el margen
        EstadoSolicitud estadoFinal;
        String notaSistema;

        // Si la desviación absoluta es menor o igual al margen, es una liquidación normal
        if (desviacionAbsoluta.compareTo(margenPermitido) <= 0) {
            estadoFinal = EstadoSolicitud.LIQUIDADA;
            notaSistema = "Liquidación normal dentro de margen (" + desviacionReal + "%).";
        } else {
            // Si está fuera de margen, distinguimos si faltó o sobró
            if (desviacionReal.compareTo(BigDecimal.ZERO) < 0) {
                estadoFinal = EstadoSolicitud.LIQUIDADA_FALTANTE;
                notaSistema = "Liquidación con FALTANTE CRÍTICO (" + desviacionReal + "%).";
            } else {
                estadoFinal = EstadoSolicitud.LIQUIDADA_EXCEDENTE;
                notaSistema = "Liquidación con EXCEDENTE FUERA DE MARGEN (" + desviacionReal + "%).";
            }
        }

        // 3. Persistir cambios
        solicitud.setEstado(estadoFinal);
        String obsActual = solicitud.getObservaciones() != null ? solicitud.getObservaciones() : "";
        solicitud.setObservaciones(obsActual + " | SISTEMA: " + notaSistema);

        ESolicitud guardada = solicitudRepository.save(solicitud);

        // 4. Retornar el detalle actualizado para la UI
        return mapToAdminDetalle(guardada);
    }


    // --- MÉTODOS PRIVADOS DE APOYO ---

    private SolicitudEnvioResponse toResponse(ESolicitudParcialidad parcialidad) {
        var personaTransportista = parcialidad.getTransportista().getPersona();
        var personaPropietario = parcialidad.getTransporte().getPropietarioPersona();

        return SolicitudEnvioResponse.builder()
                .id(parcialidad.getId())
                .solicitudId(parcialidad.getSolicitud().getId())
                .numeroEnvio(parcialidad.getNumeroViaje())
                .transportistaId(parcialidad.getTransportista().getId())
                .transportistaNombre(formatearNombreCompleto(personaTransportista))
                .licenciaNumero(parcialidad.getTransportista().getLicenciaNumero())
                .licenciaTipo(parcialidad.getTransportista().getLicenciaTipo())
                .transporteId(parcialidad.getTransporte().getId())
                .transportePlaca(parcialidad.getTransporte().getPlaca())
                .transporteMarca(parcialidad.getTransporte().getMarca())
                .transporteModelo(parcialidad.getTransporte().getModelo())
                .propietarioNombre(formatearNombreCompleto(personaPropietario))
                .fechaSalida(parcialidad.getFechaSalida())
                .fechaRecepcion(parcialidad.getFechaRecepcion())
                .pesoEnviado(parcialidad.getPesoEnviado())
                .pesoRecibido(parcialidad.getPesoRecibido())
                .estado(parcialidad.getEstado())
                .observacionesEnvio(parcialidad.getObservaciones())
                .build();
    }

    private String formatearNombreCompleto(EPersona p) {
        if (p == null) return "N/A";

        StringBuilder nombre = new StringBuilder();
        nombre.append(p.getPrimerNombre());
        if (p.getSegundoNombre() != null) nombre.append(" ").append(p.getSegundoNombre());
        nombre.append(" ").append(p.getPrimerApellido());
        if (p.getSegundoApellido() != null) nombre.append(" ").append(p.getSegundoApellido());

        return nombre.toString().toUpperCase();
    }


    // Dentro de SolicitudesEnvioService.java
    private SolicitudAdminDetalleResponse mapToAdminDetalle(ESolicitud solicitud) {
        solicitud.recalcularBalances();
        return SolicitudAdminDetalleResponse.builder()
                .id(solicitud.getId())
                .clienteNombre(formatearNombreCompleto(solicitud.getPersona()))
                .entidadNombre(solicitud.getEntidad().getNombre())
                .productoNombre(solicitud.getProducto().getNombre())
                // --- AGREGA ESTA LÍNEA ---
                .precioUnitarioPactado(solicitud.getPrecioUnitarioPactado())
                // -------------------------
                .pesoSolicitado(solicitud.getPesoSolicitado())
                .pesoTotalEnviado(solicitud.getPesoTotalEnviado())
                .pesoTotalRecibido(solicitud.getPesoTotalRecibido())
                .diferenciaPeso(solicitud.getDiferenciaPeso())
                .porcentajeDesviacion(solicitud.getPorcentajeDesviacion())
                .margenPermitido(solicitud.getMargenPermitidoPorcentaje())
                .alertaMargen(solicitud.getPorcentajeDesviacion().abs()
                        .compareTo(solicitud.getMargenPermitidoPorcentaje()) > 0 ? "FUERA" : "DENTRO")
                .estado(solicitud.getEstado().name())
                .observaciones(solicitud.getObservaciones())
                .parcialidades(solicitud.getParcialidades().stream()
                        .sorted(Comparator.comparing(ESolicitudParcialidad::getNumeroViaje))
                        .map(this::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

}