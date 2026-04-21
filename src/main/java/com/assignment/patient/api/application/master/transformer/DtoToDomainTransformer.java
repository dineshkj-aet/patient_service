package com.assignment.patient.api.application.master.transformer;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;
import org.springframework.util.ObjectUtils;

/**
 * Transforms PatientDTO objects to Patient domain objects for API request processing.
 */
public class DtoToDomainTransformer {

	/**
	 * Transforms the  PatientDTO into a Patient domain object.
	 *
	 * @param dto the PatientDTO to transform
	 * @return a Patient domain object containing the data from the PatientDTO
	 */
	public static Patient transform(PatientDTO dto) {

		if (ObjectUtils.isEmpty(dto)) {
			return null;
		}
		return Patient.builder()
				.id(dto.getId())
				.firstName(dto.getFirstName())
				.lastName(dto.getLastName())
				.address(dto.getAddress())
				.city(dto.getCity())
				.state(dto.getState())
				.zipCode(dto.getZipCode())
				.phoneNumber(dto.getPhoneNumber())
				.email(dto.getEmail())
				.build();

	}
}
