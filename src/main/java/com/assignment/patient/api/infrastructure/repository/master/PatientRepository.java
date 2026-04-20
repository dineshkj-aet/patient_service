package com.assignment.patient.api.infrastructure.repository.master;

import com.assignment.patient.api.domain.master.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	Optional<Patient> findByEmail(String email);

	boolean existsByEmail(String email);
}
