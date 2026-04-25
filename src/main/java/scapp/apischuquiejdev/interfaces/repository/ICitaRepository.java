package scapp.apischuquiejdev.interfaces.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scapp.apischuquiejdev.entity.ECita;
import scapp.apischuquiejdev.entity.EEstadoCita;

import java.util.List;

public interface ICitaRepository extends JpaRepository<ECita,Long> {


}
