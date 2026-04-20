package com.assignment.patient.api.infrastructure.repository.master;

import com.assignment.patient.api.domain.master.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	boolean existsByEmail(String email);
}
