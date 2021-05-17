package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import it.polimi.tiw.beans.*;

public class UserDAO {
	
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}
	

	public Optional<User> findUser(int id, int pwd) throws SQLException {
		
		String studentQuery = "SELECT  name, , surname FROM student WHERE id = ? AND password = ?";
		String professorQuery = "Select name, surname FROM professor WHERE id = ? AND password = ?";
		Optional<User> optUser;
		
		try {	
			optUser = executeCredentialsQuery(id, pwd, "student", studentQuery);
			if(optUser.isPresent())
				return optUser;
			
			else return executeCredentialsQuery(id, pwd, "professor", professorQuery);	
			
		}
		finally{
			
		}
			
	}
	
	private Optional<User> executeCredentialsQuery(int id, int pwd, String role, String query) throws SQLException {
		
		String action = "finding a user by id and password";
		
		try (PreparedStatement preparedStatement = con.prepareStatement(query);) {
		
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, pwd);
			
			try (ResultSet result = preparedStatement.executeQuery();) {
				
				if (!result.isBeforeFirst()) // no results, credential check failed
					return Optional.empty();
				
				else {
					
					result.next();
					User user = new User();
					user.setId(result.getInt("id"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setRole(role);
					return Optional.ofNullable(user);
					
				}
				
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + action);
			}
			
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + action);
			}
			
	}
	
		
	public Optional<User> getUserById(int id) throws SQLException{
			
			String studentQuery = "SELECT  name, , surname FROM student WHERE id = ?";
			String professorQuery = "Select name, surname FROM professor WHERE id = ?";
			Optional<User> optUser;
			
			try {	
				optUser = executeIdQuery(id , "student", studentQuery);
				if(optUser.isPresent())
					return optUser;
				
				else return executeIdQuery(id, "professor", professorQuery);	
				
			}
			finally{
				
			}
				
		}

	private Optional<User> executeIdQuery(int id, String role, String query) throws SQLException {

		
		String action = "finding a user by id";
		
		try (PreparedStatement preparedStatement = con.prepareStatement(query);) {
		
			preparedStatement.setInt(1, id);
			
			try (ResultSet result = preparedStatement.executeQuery();) {
				
				if (!result.isBeforeFirst()) // no results, credential check failed
					return Optional.empty();
				
				else {
					
					result.next();
					User user = new User();
					user.setId(result.getInt("id"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setRole(role);
					return Optional.ofNullable(user);
					
				}
				
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + action);
			}
			
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + action);
			}
			
	}

	
}
