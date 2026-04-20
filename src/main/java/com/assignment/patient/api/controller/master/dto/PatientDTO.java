package com.assignment.patient.api.controller.master.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Patient Data Transfer Object")
public class PatientDTO {

	@Schema(description = "Patient ID", example = "1")
	private Long id;

	@Schema(description = "First name", example = "John")
	@NotBlank(message = "First name is required")
	@Size(max = 40, message = "First name cannot exceed 40 characters")
	private String firstName;

	@Schema(description = "Last name", example = "Doe")
	@NotBlank(message = "Last name is required")
	@Size(max = 40, message = "Last name cannot exceed 40 characters")
	private String lastName;

	@Schema(description = "Address", example = "No 123, Park Road")
	@NotBlank(message = "Address is required")
	@Size(max = 255, message = "Address cannot exceed 255 characters")
	private String address;

	@Schema(description = "City", example = "New york")
	@NotBlank(message = "City is required")
	@Size(max = 50, message = "City cannot exceed 50 characters")
	private String city;

	@Schema(description = "State", example = "Test State")
	@Size(max = 50, message = "State cannot exceed 50 characters")
	private String state;

	@Schema(description = "Zip code", example = "0965")
	@Size(max = 20, message = "Zip code cannot exceed 20 characters")
	private String zipCode;

	@Schema(description = "Phone Number", example = "+34 123 456 789")
	@NotBlank(message = "Phone number is required")
	@Size(max = 20, message = "Phone number cannot exceed 20 characters")
	private String phoneNumber;

	@Schema(description = "Email", example = "john.doe@gmail.com")
	@NotBlank(message = "Email is required")
	@Size(max = 50, message = "Email cannot exceed 50 characters")
	private String email;

}
