package scapp.apischuquiejdev.services.persona;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.persona.PersonaCreateRequest;
import scapp.apischuquiejdev.dto.persona.PersonaResponse;
import scapp.apischuquiejdev.dto.persona.PersonaUpdateRequest;
import scapp.apischuquiejdev.entity.persona.EPersona;
import scapp.apischuquiejdev.interfaces.repository.persona.IPersonaRepository;
import scapp.apischuquiejdev.interfaces.services.persona.IPersonaService;
import scapp.apischuquiejdev.mappers.persona.PersonaMapper;


@Service
public class PersonaService implements IPersonaService {

    private final IPersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    public PersonaService(IPersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    @Transactional
    public PersonaResponse crear(PersonaCreateRequest request) {
        String cui = trimToNull(request.getCui());
        String nit = trimToNull(request.getNit());
        String email = normalizeEmail(request.getEmail());

        validarUnicosCrear(cui, nit, email);

        EPersona entity = personaMapper.toEntity(request);
        entity.setCui(cui);
        entity.setNit(nit);
        entity.setEmail(email);

        EPersona saved = personaRepository.save(entity);
        return personaMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaResponse obtenerPorId(Long id) {
        EPersona persona = personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la persona con id: " + id));

        return personaMapper.toResponse(persona);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonaResponse> listar(int page, int size, String sort, String search) {
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<EPersona> personas;

        String searchTerm = trimToNull(search);
        if (searchTerm == null) {
            personas = personaRepository.findAll(pageable);
        } else {
            personas = personaRepository
                    .findByCuiContainingIgnoreCaseOrNitContainingIgnoreCaseOrPrimerNombreContainingIgnoreCaseOrPrimerApellidoContainingIgnoreCase(
                            searchTerm, searchTerm, searchTerm, searchTerm, pageable
                    );
        }

        return personas.map(personaMapper::toResponse);
    }

    @Override
    @Transactional
    public PersonaResponse actualizar(Long id, PersonaUpdateRequest request) {
        EPersona persona = personaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la persona con id: " + id));

        String cui = trimToNull(request.getCui());
        String nit = trimToNull(request.getNit());
        String email = normalizeEmail(request.getEmail());

        validarUnicosActualizar(cui, nit, email, id);

        personaMapper.updateEntity(persona, request);
        persona.setCui(cui);
        persona.setNit(nit);
        persona.setEmail(email);

        EPersona saved = personaRepository.save(persona);
        return personaMapper.toResponse(saved);
    }

    private void validarUnicosCrear(String cui, String nit, String email) {
        if (cui != null && personaRepository.existsByCui(cui)) {
            throw new IllegalArgumentException("Ya existe una persona con el CUI ingresado.");
        }

        if (nit != null && personaRepository.existsByNit(nit)) {
            throw new IllegalArgumentException("Ya existe una persona con el NIT ingresado.");
        }

        if (email != null && personaRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe una persona con el correo ingresado.");
        }
    }

    private void validarUnicosActualizar(String cui, String nit, String email, Long id) {
        if (cui != null && personaRepository.existsByCuiAndIdNot(cui, id)) {
            throw new IllegalArgumentException("Ya existe otra persona con el CUI ingresado.");
        }

        if (nit != null && personaRepository.existsByNitAndIdNot(nit, id)) {
            throw new IllegalArgumentException("Ya existe otra persona con el NIT ingresado.");
        }

        if (email != null && personaRepository.existsByEmailAndIdNot(email, id)) {
            throw new IllegalArgumentException("Ya existe otra persona con el correo ingresado.");
        }
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        String[] parts = sort.split(",");
        String field = parts[0].trim();

        Sort.Direction direction = Sort.Direction.DESC;
        if (parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim())) {
            direction = Sort.Direction.ASC;
        }

        return Sort.by(direction, field);
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

    private String normalizeEmail(String email) {
        String value = trimToNull(email);
        if (value == null) {
            return null;
        }
        return value.toLowerCase();
    }
}