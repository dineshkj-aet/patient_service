package com.assignment.patient.api.application.master.transformer;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;

public class DtoToDomainTransformer {

	public static Patient transform(PatientDTO dto) {

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
