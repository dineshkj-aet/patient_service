package com.assignment.patient.api.controller.master;

import com.assignment.patient.api.application.master.PatientApplicationService;
import com.assignment.patient.api.controller.master.dto.PatientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Patient", description = "Patient management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping(PatientController.MASTER_PATIENTS)
public class PatientController {

	public static final String MASTER_PATIENTS = "/api/master/patients";

	private final PatientApplicationService patientApplicationService;


	@Operation(summary = "Get all Patients", description = "Retrieve a paginated list of patients with sorting options")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Patients Retrieved successfully",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = PatientDTO.class))),
			@ApiResponse(responseCode = "204", description = "No patients found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<Page<PatientDTO>> getAllPatients(
			@Parameter(description = "Page number (starts from 0)", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
			@Parameter(description = "Sort by field", example = "id") @RequestParam(defaultValue = "id") String sortBy,
			@Parameter(description = "Sort direction (asc or desc)", example = "asc") @RequestParam(defaultValue = "asc") String direction) {

		Page<PatientDTO> patients = patientApplicationService.getAllPatients(page, size, sortBy, direction);

		if (patients.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(patients);
	}

	@Operation(summary = "Get Patient by ID", description = "Retrieve a patient by their unique ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Patient retrieved successfully",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = PatientDTO.class))),
			@ApiResponse(responseCode = "404", description = "Patient not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/{id}")
	public ResponseEntity<PatientDTO> getPatientById(
			@Parameter(description = "ID of the patient to retrieve", required = true, example = "1") @PathVariable Long id) {
		try {
			PatientDTO patient = patientApplicationService.getPatientById(id);
			return ResponseEntity.ok(patient);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Create a new Patient", description = "Create a new patient with the provided details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Patient created successfully",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = PatientDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<PatientDTO> createPatient(
			@Parameter(description = "Patient payload to be created", required = true) @Valid @RequestBody PatientDTO patientDTO) {
		try {
			PatientDTO created = patientApplicationService.createPatient(patientDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Operation(summary = "Update an existing Patient", description = "Update the details of an existing patient by their ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Patient updated successfully",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = PatientDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "Patient not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PutMapping("/{id}")
	public ResponseEntity<PatientDTO> updatePatient(
			@Parameter(description = "The id of the patient to be updated", required = true) @PathVariable Long id,
			@Parameter(description = "Patient payload to be updated", required = true) @Valid @RequestBody PatientDTO patientDTO) {
		try {
			PatientDTO updated = patientApplicationService.updatePatient(id, patientDTO);
			return ResponseEntity.ok(updated);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Delete a Patient", description = "Delete an existing patient by their ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Patient not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatient(@Parameter(description = "The id of the patient to be deleted", required = true) @PathVariable Long id) {
		try {
			patientApplicationService.deletePatient(id);
			return ResponseEntity.noContent().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
