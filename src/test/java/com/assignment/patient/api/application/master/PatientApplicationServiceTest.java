package com.assignment.patient.api.application.master;

import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.assignment.patient.api.domain.master.Patient;
import com.assignment.patient.api.infrastructure.repository.master.PatientRepository;
import com.assignment.patient.api.infrastructure.service.master.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientApplicationServiceTest {

	@Mock
	private PatientService patientService;

	@Mock
	private PatientRepository patientRepository;

	@InjectMocks
	private PatientApplicationService patientApplicationService;

	private Patient patient;
	private PatientDTO patientDTO;

	@BeforeEach
	void setup() {

		patient = Patient.builder()
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

		patientDTO = PatientDTO.builder()
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
	}

	@Test
	void getAllPatients_ShouldReturnMappedDtoPage_WhenPatientsExist() {

		int page = 0;
		int size = 10;
		String sortBy = "id";
		String direction = "asc";

		Page<Patient> patientPage = new PageImpl<>(List.of(patient));

		when(patientService.fetchAllPatients(page, size, sortBy, direction)).thenReturn(patientPage);

		Page<PatientDTO> result = patientApplicationService.getAllPatients(page, size, sortBy, direction);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getFirstName()).isEqualTo("Dinesh");
		assertThat(result.getContent().get(0).getLastName()).isEqualTo("Jayasinghe");

		verify(patientService, times(1)).fetchAllPatients(page, size, sortBy, direction);

	}

	@Test
	void getAllPatientsAndEmptyShouldReturnWhenNoContent() {

		Page<PatientDTO> emptyPage = new PageImpl<>(List.of());

		when(patientApplicationService.getAllPatients(anyInt(), anyInt(), anyString(), anyString())).thenReturn(emptyPage);

		Page<PatientDTO> result = patientApplicationService.getAllPatients(0, 10, "id", "asc");

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEmpty();
		assertThat(result.getTotalElements()).isEqualTo(0);

		verify(patientService).fetchAllPatients(0, 10, "id", "asc");
	}

	@Test
	void getPatientByIdWhenPatientExistsShouldReturnPatient() {

		when(patientService.findById(1L)).thenReturn(Optional.of(patient));

		PatientDTO result = patientApplicationService.getPatientById(1L);

		assertThat(result).isNotNull();
		assertThat(result.getFirstName()).isEqualTo("Dinesh");
		assertThat(result.getLastName()).isEqualTo("Jayasinghe");

		verify(patientService).findById(1L);
	}

	@Test
	void getPatientByIdWhenPatientNotExistsShouldThrowException() {

		when(patientService.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> patientApplicationService.getPatientById(1L))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Patient not found");

		verify(patientService).findById(1L);
	}

	@Test
	void craetePatientWhenValidShouldCreateAndReturnPatient() {

		when(patientService.existsByEmail("dinesh.jayasinghe@gmail.com")).thenReturn(false);
		when(patientService.save(any(Patient.class))).thenReturn(patient);

		PatientDTO result = patientApplicationService.createPatient(patientDTO);

		assertThat(result).isNotNull();
		assertThat(result.getFirstName()).isEqualTo("Dinesh");
		assertThat(result.getLastName()).isEqualTo("Jayasinghe");

		verify(patientService).save(any(Patient.class));
	}

	@Test
	void createPatientWhenEmailExistsShouldThrowException() {

		when(patientService.existsByEmail("dinesh.jayasinghe@gmail.com")).thenReturn(true);

		assertThatThrownBy(() -> patientApplicationService.createPatient(patientDTO))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Patient with code already exists");
		verify(patientService).existsByEmail("dinesh.jayasinghe@gmail.com");

	}

	@Test
	void updatePatientWhenValidObjectShouldUpdateAndReturnPatient() {

		Long patientId = 1L;

		PatientDTO inputDto = new PatientDTO();
		inputDto.setFirstName("Dinesh");
		inputDto.setLastName("Jayasinghe");

		Patient updatedPatient = new Patient();
		updatedPatient.setId(patientId);
		updatedPatient.setFirstName("Dinesh");
		updatedPatient.setLastName("Jayasinghe");

		when(patientService.update(eq(patientId), any(Patient.class))).thenReturn(updatedPatient);

		PatientDTO result = patientApplicationService.updatePatient(patientId, inputDto);

		assertThat(result).isNotNull();
		assertThat(result.getFirstName()).isEqualTo("Dinesh");
		assertThat(result.getLastName()).isEqualTo("Jayasinghe");

		verify(patientService).update(eq(patientId), any(Patient.class));

	}

	@Test
	void updatePantientWithInvalidIdShouldReturnNull() {

		Long patientId = 99L;
		PatientDTO inputDto = new PatientDTO();

		when(patientService.update(eq(patientId), any(Patient.class))).thenReturn(null);

		assertThatThrownBy(() -> patientApplicationService.updatePatient(patientId, inputDto))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Patient not found");
	}

	@Test
	void deletePatientWhenValidIdShouldCallServiceDeleteMethod() {

		Long patientId = 1L;

		patientApplicationService.deletePatient(patientId);

		verify(patientService).deleteById(patientId);
		verify(patientService, times(1)).deleteById(patientId);
	}

	@Test
	void deletePatientWhenInvalidIdShouldThrowException() {

		Long patientId = 99L;

		doThrow(new RuntimeException("Patient does not exists:" + patientId)).when(patientService).deleteById(patientId);

		assertThatThrownBy(() -> patientApplicationService.deletePatient(patientId))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Patient does not exists:" + patientId);

		verify(patientService).deleteById(patientId);
	}

}