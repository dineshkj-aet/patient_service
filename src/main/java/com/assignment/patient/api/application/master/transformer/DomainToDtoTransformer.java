package com.assignment.patient.api.application.master.transformer;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;

public class DomainToDtoTransformer {

	public static PatientDTO transform(Patient patient) {

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
