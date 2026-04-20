package com.assignment.patient.api.infrastructure.service.master;

import com.assignment.patient.api.domain.master.Patient;
import com.assignment.patient.api.infrastructure.repository.master.PatientRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

	@Mock
	private PatientRepository patientRepository;

	@InjectMocks
	private PatientService patientService;

	private Patient patient;
	private Patient parentModified;

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

		parentModified = Patient.builder()
				.id(1L)
				.firstName("Anthoney")
				.lastName("jobson")
				.address("No 34/12, Park road")
				.city("Califonia")
				.state("CA")
				.zipCode("66778")
				.phoneNumber("+63 456 643 346")
				.email("anthoney.jobsn@gmail.com")
				.build();
	}

	@Test
	void fetchAllPationsWithPaginationSortByIdAscending() {

		List<Patient> patientList = getPatientListRandomOrder(10);
		patientList.sort(Comparator.comparing(Patient::getId));

		Page<Patient> expectedPage = new PageImpl<>(patientList);
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").ascending());

		when(patientRepository.findAll(pageRequest)).thenReturn(expectedPage);

		Page<Patient> resultPage = patientService.fetchAllPatients(0, 10, "id", "asc");

		assertThat(resultPage).isNotNull();
		assertThat(resultPage.getContent()).hasSize(10);

		assertThat(resultPage.getContent())
				.extracting(Patient::getId)
				.isSorted();

	}

	@Test
	void fetchAllPationsWithPaginationSortByIdDescending() {

		List<Patient> patientList = getPatientListRandomOrder(10);
		patientList.sort(Comparator.comparing(Patient::getId).reversed());

		Page<Patient> expectedPage = new PageImpl<>(patientList);
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());

		when(patientRepository.findAll(pageRequest)).thenReturn(expectedPage);

		Page<Patient> resultPage = patientService.fetchAllPatients(0, 10, "id", "desc");

		assertThat(resultPage).isNotNull();
		assertThat(resultPage.getContent()).hasSize(10);

		assertThat(resultPage.getContent())
				.extracting(Patient::getId)
				.isSortedAccordingTo(Comparator.reverseOrder());
	}

	@Test
	void fetchAllPationsWithPaginationSortByNameAscending() {

		List<Patient> patientList = getPatientListRandomOrder(10);
		patientList.sort(Comparator.comparing(Patient::getFirstName));

		Page<Patient> expectedPage = new PageImpl<>(patientList);
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").ascending());

		when(patientRepository.findAll(pageRequest)).thenReturn(expectedPage);

		Page<Patient> resultPage = patientService.fetchAllPatients(0, 10, "id", "asc");

		assertThat(resultPage).isNotNull();
		assertThat(resultPage.getContent()).hasSize(10);

		assertThat(resultPage.getContent())
				.extracting(Patient::getFirstName)
				.isSorted();
	}

	@Test
	void fetchAllPationsWithPaginationSortByNameDescending() {

		List<Patient> patientList = getPatientListRandomOrder(10);
		patientList.sort(Comparator.comparing(Patient::getFirstName).reversed());

		Page<Patient> expectedPage = new PageImpl<>(patientList);
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());

		when(patientRepository.findAll(pageRequest)).thenReturn(expectedPage);

		Page<Patient> resultPage = patientService.fetchAllPatients(0, 10, "id", "desc");

		assertThat(resultPage).isNotNull();
		assertThat(resultPage.getContent()).hasSize(10);

		assertThat(resultPage.getContent())
				.extracting(Patient::getFirstName)
				.isSortedAccordingTo(Comparator.reverseOrder());
	}

	@Test
	void findByIdWhenRecordExistsForGivenId() {

		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

		Optional<Patient> result = patientService.findById(1L);

		assertThat(result).isPresent();
		assertThat(result.get().getFirstName()).isEqualTo("Dinesh");

	}

	@Test
	void findByIdWhenRecordDoesNotExistsForGivenId() {

		Long invalidId = 999L;
		when(patientRepository.findById(invalidId)).thenReturn(Optional.empty());

		Optional<Patient> result = patientService.findById(invalidId);

		assertThat(result).isEmpty();
		assertThat(result).isNotPresent();

		verify(patientRepository).findById(invalidId);

	}

	@Test
	void saveShouldReturnSavedPatient() {

		when(patientRepository.save(patient)).thenReturn(patient);

		Patient result = patientService.save(patient);

		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(patient);
		verify(patientRepository).save(patient);
	}

	@Test
	void updateWhenPatientExistsShouldReturnUpdatedPatient() {

		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
		when(patientRepository.save(any(Patient.class))).thenReturn(parentModified);

		Patient result = patientService.update(1L, parentModified);

		assertThat(result).isNotNull();
		assertThat(result.getFirstName()).isEqualTo(parentModified.getFirstName());
		verify(patientRepository).findById(1L);
		verify(patientRepository).save(any(Patient.class));
	}

	@Test
	void updateWhenPatientDoesNotExistsShouldThrowException() {

		when(patientRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> patientService.update(1L, parentModified))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Patient does not exists: 1");

		verify(patientRepository).findById(1L);
		verify(patientRepository, never()).save(any());
	}

	@Test
	void deleteByIdWhenRecordExistWithGivenId() {

		patientService.deleteById(1L);

		verify(patientRepository).deleteById(1L);
	}


	@Test
	void existsByEmailWhenGivenEmailExistThenReturnTrue() {

		when(patientRepository.existsByEmail("dinesh.exists@gmial.com")).thenReturn(true);

		boolean result = patientService.existsByEmail("dinesh.exists@gmial.com");

		assertThat(result).isTrue();
		verify(patientRepository).existsByEmail("dinesh.exists@gmial.com");
	}

	@Test
	void existsByEmailWhenGivenEmailDoesNotExistThenReturnFale() {

		when(patientRepository.existsByEmail("dinesh.not.exists@gmial.com")).thenReturn(false);

		boolean result = patientService.existsByEmail("dinesh.not.exists@gmial.com");

		assertThat(result).isFalse();
		verify(patientRepository).existsByEmail("dinesh.not.exists@gmial.com");
	}

	private List<Patient> getPatientListRandomOrder(int count) {

		List<Patient> patientsList = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			patientsList.add(getRandomPatient());
		}
		return patientsList;
	}

	private Patient getRandomPatient() {
		return Instancio.of(Patient.class)
				.generate(field(Patient::getId), gen -> gen.longs().range(1L, 100L))
				.create();

	}
}