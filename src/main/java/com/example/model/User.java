package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
	private int id;
	private String name;
	private String address;
	private String email;
	private String username;
	private String password;
	private String role;
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
}
