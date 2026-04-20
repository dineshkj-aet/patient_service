package com.assignment.patient.api.application.master;

import com.assignment.patient.api.application.master.transformer.DomainToDtoTransformer;
import com.assignment.patient.api.application.master.transformer.DtoToDomainTransformer;
import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;
import com.assignment.patient.api.infrastructure.service.master.PatientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * Application service for managing patients. This service acts as a bridge between the controller layer and the service layer,
 * handling business logic between domain entities and DTOs.
 */
@Component
@RequiredArgsConstructor
public class PatientApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(PatientApplicationService.class);

	private final PatientService patientService;

	/**
	 * Retrieves a paginated list of patients, with optional sorting.
	 *
	 * @param page      the page number to retrieve (0-based index)
	 * @param size      the number of records per page
	 * @param sortBy    the field to sort by (e.g., "name", "email")
	 * @param direction the sort direction ("asc" for ascending, "desc" for descending)
	 * @return a paginated list of PatientDTOs
	 */
	public Page<PatientDTO> getAllPatients(int page, int size, String sortBy, String direction) {

		logger.debug("getAllPatients starting: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

		Page<Patient> allPatients = patientService.fetchAllPatients(page, size, sortBy, direction);

		if (allPatients == null || ObjectUtils.isEmpty(allPatients.getContent())) {
			logger.debug("getAllPatients end: No patients found");
			return Page.empty();
		}

		logger.debug("getAllPatients end: return size={}", allPatients.getSize());
		return allPatients.map(DomainToDtoTransformer::transform);

	}

	/**
	 * Retrieves a patient by their unique identifier.
	 *
	 * @param id the unique identifier of the patient
	 * @return the PatientDTO corresponding to the given id
	 * @throws RuntimeException if no patient is found with the given id
	 */
	public PatientDTO getPatientById(Long id) {

		logger.debug("getPatientById starting: id={}", id);

		Patient patient = patientService.findById(id)
				.orElseThrow(() -> new RuntimeException("Patient not found"));

		logger.debug("getPatientById end: patient={}", patient);

		return DomainToDtoTransformer.transform(patient);
	}

	/**
	 * Creates a new patient record.
	 *
	 * @param patient the PatientDTO containing the details of the patient to be created
	 * @return the created PatientDTO
	 * @throws RuntimeException if a patient with the same email already exists
	 */
	public PatientDTO createPatient(PatientDTO patient) {

		logger.debug("createPatient starting: patient={}", patient);

		if (patientService.existsByEmail(patient.getEmail())) {
			throw new RuntimeException("Patient with the email already exists");
		}
		return DomainToDtoTransformer.transform(patientService.save(DtoToDomainTransformer.transform(patient)));
	}

	/**
	 * Updates an existing patient record.
	 *
	 * @param id      the unique identifier of the patient to be updated
	 * @param patient the PatientDTO containing the updated details of the patient
	 * @return the updated PatientDTO
	 * @throws RuntimeException if no patient is found with the given id
	 */
	public PatientDTO updatePatient(Long id, PatientDTO patient) {

		logger.debug("updatePatient starting: id={}, patient={}", id, patient);

		Patient updatedPatient = patientService.update(id, DtoToDomainTransformer.transform(patient));
		if (ObjectUtils.isEmpty(updatedPatient)) {
			logger.debug("updatePatient end: No patient found with id={}", id);
			throw new RuntimeException("Patient not found");
		}
		return DomainToDtoTransformer.transform(updatedPatient);
	}

	/**
	 * Deletes a patient record by its unique identifier.
	 *
	 * @param id the unique identifier of the patient to be deleted
	 */
	public void deletePatient(Long id) {

		logger.debug("deletePatient starting: id={}", id);

		patientService.deleteById(id);
	}

}
