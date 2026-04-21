package com.assignment.patient.api.application.master.transformer;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainToDtoTransformerTest {

	@Test
	void transformWithCompleteDomainWithAllFields() {
		Patient patient = Patient.builder()
				.id(1L)
				.firstName("Dinesh")
				.lastName("Jayasinghe")
				.address("No 123, Test street")
				.city("NewYork")
				.state("NY")
				.zipCode("12345")
				.phoneNumber("+55 123 3456 789")
				.email("dinesh.jayasinghe@gmail.com")
				.build();

		PatientDTO patientDTO = DomainToDtoTransformer.transform(patient);

		assertThat(patientDTO).isNotNull();
		assertThat(patientDTO.getId()).isEqualTo(patient.getId());
		assertThat(patientDTO.getFirstName()).isEqualTo(patient.getFirstName());
		assertThat(patientDTO.getLastName()).isEqualTo(patient.getLastName());
		assertThat(patientDTO.getAddress()).isEqualTo(patient.getAddress());
		assertThat(patientDTO.getCity()).isEqualTo(patient.getCity());
		assertThat(patientDTO.getState()).isEqualTo(patient.getState());
		assertThat(patientDTO.getZipCode()).isEqualTo(patient.getZipCode());
		assertThat(patientDTO.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
		assertThat(patientDTO.getEmail()).isEqualTo(patient.getEmail());

	}

	@Test
	void transformWithDomainWithNullOptionalFields() {
		Patient patient = Patient.builder()
				.id(1L)
				.firstName("Dinesh")
				.lastName("Jayasinghe")
				.address(null)
				.city(null)
				.state(null)
				.zipCode(null)
				.phoneNumber(null)
				.email(null)
				.build();

		PatientDTO patientDTO = DomainToDtoTransformer.transform(patient);

		assertThat(patientDTO).isNotNull();
		assertThat(patientDTO.getId()).isEqualTo(patient.getId());
		assertThat(patientDTO.getFirstName()).isEqualTo(patient.getFirstName());
		assertThat(patientDTO.getLastName()).isEqualTo(patient.getLastName());
		assertThat(patientDTO.getAddress()).isNull();
		assertThat(patientDTO.getCity()).isNull();
		assertThat(patientDTO.getState()).isNull();
		assertThat(patientDTO.getZipCode()).isNull();
		assertThat(patientDTO.getPhoneNumber()).isNull();
		assertThat(patientDTO.getEmail()).isNull();

	}

	@Test
	void transformWithNullDomainShouldReturnNullDto() {
		Patient patient = null;

		PatientDTO patientDTO = DomainToDtoTransformer.transform(patient);

		assertThat(patientDTO).isNull();
	}

	@Test
	void transformWithDefaultDomainShouldReturnNullDto() {
		Patient patient = Patient.builder().build();

		PatientDTO patientDTO = DomainToDtoTransformer.transform(patient);

		assertThat(patientDTO).isNotNull();
		assertThat(patientDTO.getId()).isNull();
		assertThat(patientDTO.getFirstName()).isNull();
		assertThat(patientDTO.getLastName()).isNull();
		assertThat(patientDTO.getAddress()).isNull();
		assertThat(patientDTO.getCity()).isNull();
		assertThat(patientDTO.getState()).isNull();
		assertThat(patientDTO.getZipCode()).isNull();
		assertThat(patientDTO.getPhoneNumber()).isNull();
		assertThat(patientDTO.getEmail()).isNull();
	}

}