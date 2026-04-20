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

@Service
@Transactional
@AllArgsConstructor
public class PatientService {

	private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

	private final PatientRepository patientRepository;

	@Transactional(readOnly = true)
	public Page<Patient> getAllPatients(int page, int size, String sortBy, String direction) {

		logger.debug("getAllPatients starting: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

		Sort sort = direction.equalsIgnoreCase("asc") ?
				Sort.by(sortBy).ascending() :
				Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(page, size, sort);

		logger.debug("getAllPatients end: pageSize={}", pageable.getPageSize());

		return patientRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<Patient> findById(Long id) {
		return patientRepository.findById(id);
	}

	public Patient save(Patient patient) {
		return patientRepository.save(patient);
	}

	public void deleteById(Long id) {
		patientRepository.deleteById(id);
	}

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
				.orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
	}
}
