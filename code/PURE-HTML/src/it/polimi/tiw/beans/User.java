package it.polimi.tiw.beans;


public class User {
	
	private int id;
	private final String email;
	private final String name;
	private final String surname;
	private final String role;
	private final String info;
	
	public User(int id, String email, String name, String surname, String role, String info) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.role = role;
		this.info = info;
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
	
	public String getInfo() {
		return info;
	}
	
	
}

