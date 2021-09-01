package it.polimi.tiw.beans;


public class User {
	
	private int id;
	private final String email;
	private final String name;
	private final String surname;
	private final String role;
	
	public User(int id, String email, String name, String surname, String role) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.role = role;
	}
	
	public User() {
		this.email = "";
		this.name = "";
		this.surname = "";
		this.role = "";
	}
	
	
	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}

	public String getRole() {
		return role;
	}
	
	
}

