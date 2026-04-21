package com.assignment.patient.api.infrastructure.service.master;

import com.assignment.patient.api.domain.master.Patient;
import com.assignment.patient.api.infrastructure.repository.master.PatientRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * the PatientService eService class for managing patients server side business logic implementations. This service acts as a bridge between the application layer and the domain layer,
 * handling business logic and transformations.
 */
@Service
@Transactional
@AllArgsConstructor
public class PatientService {

	private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

	private final PatientRepository patientRepository;

	/**
	 * Fetch the  paginated list of patients, with given page, size, sortby and sort direction.
	 *
	 * @param page      the page number to retrieve (0-based index)
	 * @param size      the number of records per page
	 * @param sortBy    the field to sort by (e.g., "firstName", "email")
	 * @param direction the sort direction ("asc" for ascending, "desc" for descending)
	 * @return a paginated list of patients
	 */
	@Transactional(readOnly = true)
	public Page<Patient> fetchAllPatients(int page, int size, String sortBy, String direction) {

		logger.debug("getAllPatients starting: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

		Sort sort = direction.equalsIgnoreCase("asc") ?
				Sort.by(sortBy).ascending() :
				Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		logger.debug("getAllPatients end: pageSize={}", pageable.getPageSize());

		return patientRepository.findAll(pageable);
	}

	/**
	 * Retrieves a patient by their id(primary key).
	 *
	 * @param id the unique identifier of the patient
	 * @return an Optional containing the patient if found, or empty if not found
	 */
	@Transactional(readOnly = true)
	public Optional<Patient> findById(Long id) {
		return patientRepository.findById(id);
	}

	/**
	 * Saves a new patient record to the database.
	 *
	 * @param patient the patient entity to be saved
	 * @return the saved patient entity
	 */
	public Patient save(Patient patient) {
		return patientRepository.save(patient);
	}

	/**
	 * Deletes a patient record by patient id (primary key id).
	 *
	 * @param id the unique identifier of the patient to be deleted
	 */
	public void deleteById(Long id) {
		patientRepository.deleteById(id);
	}

	/**
	 * Checks and update the patient record in database, if patient record not available, return with RunTimeException..
	 *
	 * @param email the email address to check for existence
	 * @return true if a patient with the given email exists, false otherwise
	 */
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return patientRepository.existsByEmail(email);
	}

	public Patient update(Long id, Patient patient) {

		logger.debug("update patient: id={},patient={}", id, patient);

		return patientRepository.findById(id)
				.map(existing -> {
					existing.setFirstName(patient.getFirstName());
					existing.setLastName(patient.getLastName());
					existing.setAddress(patient.getAddress());
					existing.setCity(patient.getCity());
					existing.setState(patient.getState());
					existing.setZipCode(patient.getZipCode());
					existing.setPhoneNumber(patient.getPhoneNumber());
					existing.setEmail(patient.getEmail());
					return patientRepository.save(existing);
				})
				.orElseThrow(() -> new RuntimeException("Patient does not exists: " + id));
	}
}
