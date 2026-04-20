package com.assignment.patient.api.application.master.transformer;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;
import org.springframework.util.ObjectUtils;

/**
 * Transforms Patient domain objects to PatientDTO objects for API responses.
 */
public class DomainToDtoTransformer {

	/**
	 * Transforms a Patient domain object into a PatientDTO.
	 * @param patient the Patient domain object to transform
	 * @return a PatientDTO containing the data from the Patient domain object
	 */
	public static PatientDTO transform(Patient patient) {

		if(ObjectUtils.isEmpty(patient)){
			return null;
		}
		return PatientDTO.builder()
				.id(patient.getId())
				.firstName(patient.getFirstName())
				.lastName(patient.getLastName())
				.address(patient.getAddress())
				.city(patient.getCity())
				.state(patient.getState())
				.zipCode(patient.getZipCode())
				.phoneNumber(patient.getPhoneNumber())
				.email(patient.getEmail())
				.build();
	}
}
