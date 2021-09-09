package it.polimi.tiw.simplebeans;

public class UserPacket {

	private final String name;
	private final int id;
	private final String role;
	
	public UserPacket(String name, int id, String role) {
		this.name = name;
		this.id = id;
		this.role = role;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getRole() {
		return role;
	}
}