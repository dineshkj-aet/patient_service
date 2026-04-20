package com.assignment.patient.api.application.master.transformer;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DtoToDomainTransformerTest {

	@Test
	void transformWithCompleteDomainWithAllFields() {
		PatientDTO patientDTO = PatientDTO.builder()
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

		Patient patient = DtoToDomainTransformer.transform(patientDTO);

		assertThat(patient).isNotNull();
		assertThat(patient.getId()).isEqualTo(patientDTO.getId());
		assertThat(patient.getFirstName()).isEqualTo(patientDTO.getFirstName());
		assertThat(patient.getLastName()).isEqualTo(patientDTO.getLastName());
		assertThat(patient.getAddress()).isEqualTo(patientDTO.getAddress());
		assertThat(patient.getCity()).isEqualTo(patientDTO.getCity());
		assertThat(patient.getState()).isEqualTo(patientDTO.getState());
		assertThat(patient.getZipCode()).isEqualTo(patientDTO.getZipCode());
		assertThat(patient.getPhoneNumber()).isEqualTo(patientDTO.getPhoneNumber());
		assertThat(patient.getEmail()).isEqualTo(patientDTO.getEmail());

	}

	@Test
	void transformWithDomainWithNullOptionalFields() {
		PatientDTO patientDTO = PatientDTO.builder()
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

		Patient patient = DtoToDomainTransformer.transform(patientDTO);

		assertThat(patient).isNotNull();
		assertThat(patient.getId()).isEqualTo(patientDTO.getId());
		assertThat(patient.getFirstName()).isEqualTo(patientDTO.getFirstName());
		assertThat(patient.getLastName()).isEqualTo(patientDTO.getLastName());
		assertThat(patient.getAddress()).isNull();
		assertThat(patient.getCity()).isNull();
		assertThat(patient.getState()).isNull();
		assertThat(patient.getZipCode()).isNull();
		assertThat(patient.getPhoneNumber()).isNull();
		assertThat(patient.getEmail()).isNull();

	}

	@Test
	void transformWithNullDomainShouldReturnNullDto() {
		PatientDTO patientDTO = null;

		Patient patient = DtoToDomainTransformer.transform(patientDTO);

		assertThat(patient).isNull();
	}

	@Test
	void transformWithDefaultDomainShouldReturnNullDto() {
		PatientDTO patientDTO = PatientDTO.builder().build();

		Patient patient = DtoToDomainTransformer.transform(patientDTO);

		assertThat(patient).isNotNull();
		assertThat(patient.getId()).isNull();
		assertThat(patient.getFirstName()).isNull();
		assertThat(patient.getLastName()).isNull();
		assertThat(patient.getAddress()).isNull();
		assertThat(patient.getCity()).isNull();
		assertThat(patient.getState()).isNull();
		assertThat(patient.getZipCode()).isNull();
		assertThat(patient.getPhoneNumber()).isNull();
		assertThat(patient.getEmail()).isNull();
	}

}