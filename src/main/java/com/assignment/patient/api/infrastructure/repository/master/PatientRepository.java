package com.assignment.patient.api.infrastructure.repository.master;

import com.assignment.patient.api.domain.master.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

	/**
	 * Checks if a patient with the given email already exists in the database.
	 *
	 * @param email the email address to check for existence
	 * @return true if a patient with the given email exists, false otherwise
	 */
	boolean existsByEmail(String email);
}
