package scapp.apischuquiejdev.services.transportista;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.persona.PersonaResponse;
import scapp.apischuquiejdev.dto.transportista.TransportistaCreateRequest;
import scapp.apischuquiejdev.dto.transportista.TransportistaResponse;
import scapp.apischuquiejdev.entity.transportista.EEstadoTransportista;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.entity.transportista.ETransportista;
import scapp.apischuquiejdev.interfaces.repository.transportista.IEstadoTransportistaRepository;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRolRepository;
import scapp.apischuquiejdev.interfaces.repository.IRolRepository;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRepository;
import scapp.apischuquiejdev.interfaces.repository.transportista.ITransportistaRepository;
import scapp.apischuquiejdev.interfaces.services.persona.IPersonaService;
import scapp.apischuquiejdev.interfaces.services.transportista.ITransportistaService;
import scapp.apischuquiejdev.mappers.TransportistaMapper;

import java.util.List;

@Service
public class TransportistaService implements ITransportistaService {

    private static final String ROL_TRANSPORTISTA = "TRANSPORTISTA";
    private static final String ESTADO_DEFAULT_CODIGO = "PENDIENTE_APROBACION";

    private final IPersonaService personaService;
    private final IPersonaRepository personaRepository;
    private final ITransportistaRepository transportistaRepository;
    private final IEstadoTransportistaRepository estadoTransportistaRepository;
    private final IRolRepository rolRepository;
    private final IPersonaRolRepository personaRolRepository;
    private final TransportistaMapper transportistaMapper;

    public TransportistaService(
            IPersonaService personaService,
            IPersonaRepository personaRepository,
            ITransportistaRepository transportistaRepository,
            IEstadoTransportistaRepository estadoTransportistaRepository,
            IRolRepository rolRepository,
            IPersonaRolRepository personaRolRepository,
            TransportistaMapper transportistaMapper
    ) {
        this.personaService = personaService;
        this.personaRepository = personaRepository;
        this.transportistaRepository = transportistaRepository;
        this.estadoTransportistaRepository = estadoTransportistaRepository;
        this.rolRepository = rolRepository;
        this.personaRolRepository = personaRolRepository;
        this.transportistaMapper = transportistaMapper;
    }

    @Override
    @Transactional
    public TransportistaResponse crearOActualizarTransportista(TransportistaCreateRequest request, String asignadoPor) {
        EPersona persona = resolverPersonaEntity(request);

        asegurarRolTransportista(persona.getId(), asignadoPor);

        EEstadoTransportista estadoDefault = estadoTransportistaRepository.findByCodigo(ESTADO_DEFAULT_CODIGO)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el estado transportista con código: " + ESTADO_DEFAULT_CODIGO
                ));

        ETransportista transportista = transportistaRepository.findByPersona_Id(persona.getId())
                .orElseGet(() -> {
                    ETransportista t = new ETransportista();
                    t.setPersona(persona);
                    t.setEstadoTransportista(estadoDefault);
                    t.setAprobado(false);
                    t.setActivo(true);
                    return t;
                });

        transportista.setLicenciaNumero(trimToNull(request.getLicenciaNumero()));
        transportista.setLicenciaTipo(trimToNull(request.getLicenciaTipo()));
        transportista.setLicenciaVencimiento(request.getLicenciaVencimiento());

        if (transportista.getEstadoTransportista() == null) {
            transportista.setEstadoTransportista(estadoDefault);
        }

        if (transportista.getAprobado() == null) {
            transportista.setAprobado(false);
        }

        if (transportista.getActivo() == null) {
            transportista.setActivo(true);
        }

        ETransportista saved = transportistaRepository.save(transportista);
        return transportistaMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportistaResponse> obtenerTodos() {
        List<ETransportista> transportistas = transportistaRepository.findAll();
        return transportistaMapper.toResponseList(transportistas);
    }

    @Override
    @Transactional(readOnly = true)
    public TransportistaResponse obtenerPorId(Long id) {
        ETransportista transportista = transportistaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el transportista con id: " + id));

        return transportistaMapper.toResponse(transportista);
    }

    private EPersona resolverPersonaEntity(TransportistaCreateRequest request) {
        if (request.getPersonaId() != null) {
            return personaRepository.findById(request.getPersonaId())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la persona con id: " + request.getPersonaId()));
        }

        if (request.getPersona() == null) {
            throw new IllegalArgumentException("Debe enviar personaId o el objeto persona.");
        }

        PersonaResponse created = personaService.crear(request.getPersona());

        return personaRepository.findById(created.getId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la persona recién creada con id: " + created.getId()));
    }

    private void asegurarRolTransportista(Long personaId, String asignadoPor) {
        var rol = rolRepository.findByCodigo(ROL_TRANSPORTISTA)
                .orElseThrow(() -> new IllegalArgumentException("No existe el rol con codigo: " + ROL_TRANSPORTISTA));

        boolean exists = personaRolRepository.existsById_PersonaIdAndId_RolId(personaId, rol.getId());
        if (exists) {
            return;
        }

        personaRolRepository.insertPersonaRol(personaId, rol.getId(), asignadoPor);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String v = value.trim();
        return v.isEmpty() ? null : v;
    }
}