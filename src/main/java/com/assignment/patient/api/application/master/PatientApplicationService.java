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

@Component
@RequiredArgsConstructor
public class PatientApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(PatientApplicationService.class);

	private final PatientService patientService;

	public Page<PatientDTO> getAllPatients(int page, int size, String sortBy, String direction) {

		logger.debug("getAllPatients starting: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

		Page<Patient> allPatients = patientService.getAllPatients(page, size, sortBy, direction);

		return allPatients.map(DomainToDtoTransformer::transform);

	}

	public PatientDTO getPatientById(Long id) {

		logger.debug("getPatientById starting: id={}", id);

		Patient patient = patientService.findById(id)
				.orElseThrow(() -> new RuntimeException("Patient not found"));

		return DomainToDtoTransformer.transform(patient);
	}

	public PatientDTO createPatient(PatientDTO patient) {

		logger.debug("createPatient starting: patient={}", patient);

		if (patientService.existsByEmail(patient.getEmail())) {
			throw new RuntimeException("Patient with code already exists");
		}
		return DomainToDtoTransformer.transform(patientService.save(DtoToDomainTransformer.transform(patient)));
	}

	public PatientDTO updatePatient(Long id, PatientDTO patient) {

		logger.debug("updatePatient starting: id={}, patient={}", id, patient);

		return DomainToDtoTransformer.transform(patientService.update(id, DtoToDomainTransformer.transform(patient)));
	}

	public void deletePatient(Long id) {

		logger.debug("deletePatient starting: id={}", id);

		patientService.deleteById(id);
	}

}
