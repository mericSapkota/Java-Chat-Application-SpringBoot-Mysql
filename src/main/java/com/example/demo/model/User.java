package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique=true, nullable=false)
	private String userName;
	@Column(unique=true,nullable=false)
	private String fullName;
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	@Lob//Binary Large Objects
	@Column(columnDefinition = "MEDIUMBLOB")//Binary Large Objects
	private String img;
	private String virificationToken;
	private boolean isVerified;

}
