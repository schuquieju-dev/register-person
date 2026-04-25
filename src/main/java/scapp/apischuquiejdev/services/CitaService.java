package scapp.apischuquiejdev.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scapp.apischuquiejdev.dto.request.CitaCreateRequest;
import scapp.apischuquiejdev.dto.response.CitaResponse;

import scapp.apischuquiejdev.entity.ECita;
import scapp.apischuquiejdev.interfaces.repository.ICitaRepository;
import scapp.apischuquiejdev.interfaces.services.ICitaService;
import scapp.apischuquiejdev.mappers.CitaMapper;
import scapp.apischuquiejdev.util.BusinessException;

import java.util.List;
@Service
public class CitaService implements ICitaService {

    private final ICitaRepository citaRepository;
    private final CitaMapper citaMapper;

    public CitaService(ICitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        return citaRepository.findAll()
                .stream()
                .map(citaMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public CitaResponse crear(CitaCreateRequest request) {



        ECita cita = citaMapper.toEntity(request);
        ECita saved = citaRepository.save(cita);

        return citaMapper.toResponse(saved);
    }
}
