package scapp.apischuquiejdev.services.transporte;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.TransporteCreateRequest;
import scapp.apischuquiejdev.dto.TransporteResponse;
import scapp.apischuquiejdev.entity.transporte.EEstadoTransporte;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.entity.transporte.ETransporte;
import scapp.apischuquiejdev.interfaces.repository.transporte.IEstadoTransporteRepository;
import scapp.apischuquiejdev.interfaces.repository.transporte.ITransporteRepository;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRepository;
import scapp.apischuquiejdev.interfaces.services.transporte.ITransporteService;
import scapp.apischuquiejdev.mappers.TransporteMapper;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TransporteService implements ITransporteService {

    private static final String ESTADO_DEFAULT_CODIGO = "PENDIENTE_APROBACION";

    private final ITransporteRepository transporteRepository;
    private final IEstadoTransporteRepository estadoTransporteRepository;
    private final IPersonaRepository personaRepository;
    private final TransporteMapper transporteMapper;

    public TransporteService(
            ITransporteRepository transporteRepository,
            IEstadoTransporteRepository estadoTransporteRepository,
            IPersonaRepository personaRepository,
            TransporteMapper transporteMapper
    ) {
        this.transporteRepository = transporteRepository;
        this.estadoTransporteRepository = estadoTransporteRepository;
        this.personaRepository = personaRepository;
        this.transporteMapper = transporteMapper;
    }

    @Override
    @Transactional
    public TransporteResponse crear(TransporteCreateRequest request) {
        EPersona propietario = personaRepository.findById(request.getPropietarioPersonaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró la persona propietaria con id: " + request.getPropietarioPersonaId()
                ));

        String placa = normalizeUpper(request.getPlaca());
        String marca = trimToNull(request.getMarca());
        String modelo = trimToNull(request.getModelo());
        String color = trimToNull(request.getColor());
        String tipo = trimToNull(request.getTipo());

        validarCrear(placa, request.getAnio());

        EEstadoTransporte estadoDefault = estadoTransporteRepository.findByCodigo(ESTADO_DEFAULT_CODIGO)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el estado transporte con código: " + ESTADO_DEFAULT_CODIGO
                ));

        ETransporte entity = new ETransporte();
        entity.setPropietarioPersona(propietario);
        entity.setPlaca(placa);
        entity.setMarca(marca);
        entity.setModelo(modelo);
        entity.setAnio(request.getAnio());
        entity.setColor(color);
        entity.setTipo(tipo);
        entity.setCapacidadCargaKg(request.getCapacidadCargaKg());
        entity.setEstadoTransporte(estadoDefault);
        entity.setAprobado(false);
        entity.setActivo(true);
        entity.setAprobadoPor(null);
        entity.setAprobadoAt(null);
        entity.setObservacionAprobacion(null);

        ETransporte saved = transporteRepository.save(entity);
        return transporteMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransporteResponse> obtenerTodos() {
        List<ETransporte> transportes = transporteRepository.findAll();
        return transporteMapper.toResponseList(transportes);
    }

    @Override
    @Transactional(readOnly = true)
    public TransporteResponse obtenerPorId(Long id) {
        ETransporte transporte = transporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el transporte con id: " + id));

        return transporteMapper.toResponse(transporte);
    }

    private void validarCrear(String placa, Integer anio) {
        if (transporteRepository.existsByPlacaIgnoreCase(placa)) {
            throw new IllegalArgumentException("Ya existe un transporte con la placa ingresada.");
        }

        if (anio != null) {
            int currentYear = OffsetDateTime.now().getYear();
            if (anio < 1950 || anio > currentYear + 1) {
                throw new IllegalArgumentException("El año del transporte es inválido.");
            }
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed;
    }

    private String normalizeUpper(String value) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            return null;
        }

        return normalized.toUpperCase();
    }
}


