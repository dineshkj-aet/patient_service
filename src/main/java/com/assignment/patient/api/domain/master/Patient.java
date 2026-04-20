package com.assignment.patient.api.domain.master;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient", schema = "master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false, length = 40)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 40)
	private String lastName;

	@Column(name = "address", nullable = false, length = 255)
	private String address;

	@Column(name = "city", nullable = false, length = 50)
	private String city;

	@Column(name = "state", length = 50)
	private String state;

	@Column(name = "zip_code", length = 20)
	private String zipCode;

	@Column(name = "phone_number", nullable = false, length = 20)
	private String phoneNumber;

	@Column(name = "email", nullable = false, length = 50)
	private String email;
}
