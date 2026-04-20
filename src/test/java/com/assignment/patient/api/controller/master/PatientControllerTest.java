package com.assignment.patient.api.controller.master;

import com.assignment.patient.api.application.master.PatientApplicationService;
import com.assignment.patient.api.controller.master.dto.PatientDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

	public static final String PATH = "/api/master/patients";
	public static final String CONTENT_TYPE = "application/json";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PatientApplicationService patientApplicationService;

	@Autowired
	private ObjectMapper objectMapper;

	private PatientDTO patientDTO;

	@BeforeEach
	void setup() {

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
	void getAllPatientsAndShouldReturnPageOfPatients() throws Exception {

		List<PatientDTO> patientDTOList = List.of(patientDTO);
		Page<PatientDTO> patientDTOPage = new PageImpl<>(patientDTOList);

		when(patientApplicationService.getAllPatients(0, 10, "id", "asc")).thenReturn(patientDTOPage);

		mockMvc.perform(get(PATH)
						.param("page", "0")
						.param("size", "10")
						.param("sortBy", "id")
						.param("direction", "asc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(patientDTO.getId()))
				.andExpect(jsonPath("$.content[0].firstName").value(patientDTO.getFirstName()))
				.andExpect(jsonPath("$.content[0].lastName").value(patientDTO.getLastName()))
				.andExpect(jsonPath("$.content[0].address").value(patientDTO.getAddress()))
				.andExpect(jsonPath("$.content[0].city").value(patientDTO.getCity()))
				.andExpect(jsonPath("$.content[0].state").value(patientDTO.getState()))
				.andExpect(jsonPath("$.content[0].zipCode").value(patientDTO.getZipCode()))
				.andExpect(jsonPath("$.content[0].phoneNumber").value(patientDTO.getPhoneNumber()))
				.andExpect(jsonPath("$.content[0].email").value(patientDTO.getEmail()));

	}

	@Test
	void getAllPatientsAndEmptyShouldReturnWhenNoContent() throws Exception {

		Page<PatientDTO> emptyPage = new PageImpl<>(List.of());

		when(patientApplicationService.getAllPatients(anyInt(), anyInt(), anyString(), anyString())).thenReturn(emptyPage);

		mockMvc.perform(get(PATH)).andExpect(status().isNoContent());
	}

	@Test
	void getPatientByIdWhenPatientExistsShouldReturnPatient() throws Exception {

		when(patientApplicationService.getPatientById(1L)).thenReturn(patientDTO);

		mockMvc.perform(get(PATH + "/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(patientDTO.getId()))
				.andExpect(jsonPath("$.firstName").value(patientDTO.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(patientDTO.getLastName()))
				.andExpect(jsonPath("$.address").value(patientDTO.getAddress()))
				.andExpect(jsonPath("$.city").value(patientDTO.getCity()))
				.andExpect(jsonPath("$.state").value(patientDTO.getState()))
				.andExpect(jsonPath("$.zipCode").value(patientDTO.getZipCode()))
				.andExpect(jsonPath("$.phoneNumber").value(patientDTO.getPhoneNumber()))
				.andExpect(jsonPath("$.email").value(patientDTO.getEmail()));
	}

	@Test
	void getPatientByIdWhenPatientDoesNotExistShouldReturnNotFound() throws Exception {

		when(patientApplicationService.getPatientById(1L)).thenThrow(new RuntimeException("Patient not found"));

		mockMvc.perform(get(PATH + "/1")).andExpect(status().isNotFound());
	}

	@Test
	void createPatientWithValidPatientObjectShouldReturnCreated() throws Exception {

		when(patientApplicationService.createPatient(any(PatientDTO.class))).thenReturn(patientDTO);

		mockMvc.perform(post(PATH)
						.contentType(CONTENT_TYPE)
						.content(objectMapper.writeValueAsString(patientDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(patientDTO.getId()))
				.andExpect(jsonPath("$.firstName").value(patientDTO.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(patientDTO.getLastName()))
				.andExpect(jsonPath("$.address").value(patientDTO.getAddress()))
				.andExpect(jsonPath("$.city").value(patientDTO.getCity()))
				.andExpect(jsonPath("$.state").value(patientDTO.getState()))
				.andExpect(jsonPath("$.zipCode").value(patientDTO.getZipCode()))
				.andExpect(jsonPath("$.phoneNumber").value(patientDTO.getPhoneNumber()))
				.andExpect(jsonPath("$.email").value(patientDTO.getEmail()));
	}

	@Test
	void createPatientWithInvalidPatientObjectShouldReturnBadRequest() throws Exception {

		when(patientApplicationService.createPatient(any(PatientDTO.class)))
				.thenThrow(new RuntimeException("Invalid input data"));

		mockMvc.perform(post(PATH)
						.contentType(CONTENT_TYPE)
						.content(objectMapper.writeValueAsString(patientDTO)))
				.andExpect(status().isBadRequest());

	}

	@Test
	void updatePatientWithValidPatientObjectShouldReturnUpdatedPatient() throws Exception {

		PatientDTO updatedPatientDTO = PatientDTO.builder()
				.id(1L)
				.firstName("Anne")
				.lastName("Basent")
				.address("No 456, Park Street")
				.city("Melbourne")
				.state("MB")
				.zipCode("87675")
				.phoneNumber("+34 223 445 667")
				.email("anne.basent@gmail.com")
				.build();

		when(patientApplicationService.updatePatient(eq(1L), any(PatientDTO.class))).thenReturn(updatedPatientDTO);

		mockMvc.perform(put(PATH + "/1")
						.contentType(CONTENT_TYPE)
						.content(objectMapper.writeValueAsString(updatedPatientDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedPatientDTO.getId()))
				.andExpect(jsonPath("$.firstName").value(updatedPatientDTO.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(updatedPatientDTO.getLastName()))
				.andExpect(jsonPath("$.address").value(updatedPatientDTO.getAddress()))
				.andExpect(jsonPath("$.city").value(updatedPatientDTO.getCity()))

				.andExpect(jsonPath("$.state").value(updatedPatientDTO.getState()))
				.andExpect(jsonPath("$.zipCode").value(updatedPatientDTO.getZipCode()))
				.andExpect(jsonPath("$.phoneNumber").value(updatedPatientDTO.getPhoneNumber()))
				.andExpect(jsonPath("$.email").value(updatedPatientDTO.getEmail()));


	}

	@Test
	void updatePatientWithInvalidPatientObjectShouldReturnNotFound() throws Exception {

		when(patientApplicationService.updatePatient(eq(1L), any(PatientDTO.class)))
				.thenThrow(new RuntimeException("Patient does not exists:" + 1L));

		mockMvc.perform(put(PATH + "/1").contentType(CONTENT_TYPE).content(objectMapper.writeValueAsString(patientDTO)))
				.andExpect(status().isNotFound());

	}

	@Test
	void deletePatientWithExistingPatientShouldReturnNoContent() throws Exception {

		doNothing().when(patientApplicationService).deletePatient(1L);

		mockMvc.perform(delete(PATH + "/1")).andExpect(status().isNoContent());
	}

	@Test
	void deletePatientWithNonExistingPatientShouldReturnNotFound() throws Exception {

		doThrow(new RuntimeException("Patient does not exists:" + 1L)).when(patientApplicationService).deletePatient(1L);

		mockMvc.perform(delete(PATH + "/1")).andExpect(status().isNotFound());
	}


}