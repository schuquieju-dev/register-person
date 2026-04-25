package scapp.apischuquiejdev.interfaces.services;

import scapp.apischuquiejdev.dto.request.CitaCreateRequest;
import scapp.apischuquiejdev.dto.response.CitaResponse;
import scapp.apischuquiejdev.entity.ECita;

import java.util.List;

public interface ICitaService {

    List<CitaResponse> listar();
    CitaResponse crear(CitaCreateRequest request);
}
