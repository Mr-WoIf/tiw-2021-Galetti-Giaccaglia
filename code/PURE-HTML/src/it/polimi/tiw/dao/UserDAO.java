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
	

	public Optional<User> findUser(int id, String pwd) throws SQLException {
		
		String studentQuery = "SELECT * FROM unidb.student WHERE id = ? AND password = ?";
		String professorQuery = "Select * FROM unidb.professor WHERE id = ? AND password = ?";
		Optional<User> optUser;

		optUser = executeCredentialsQuery(id, pwd, "student", studentQuery);
		if(optUser.isPresent())
			return optUser;

		else return executeCredentialsQuery(id, pwd, "professor", professorQuery);

	}
	
	private Optional<User> executeCredentialsQuery(int id, String pwd, String role, String query) throws SQLException {
		
		String action = " finding a user by id and password";
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
			
		try {
		    preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, pwd);
			result = preparedStatement.executeQuery();
				
				if (!result.isBeforeFirst()) // no results, credential check failed
					return Optional.empty();
				
				else {
					
					while(result.next()) {
					
					User user = new User(result.getInt("id"), result.getString("mail"), result.getString("name"), result.getString("surname"), role);
					return Optional.ofNullable(user);	
					}
				}
				
			} catch(SQLException e) {
				throw new SQLException("Error accessing the DB when" + action);
			}
				finally {
					try {
						  assert result != null;
						result.close();
					}
				catch (Exception e) {
				throw new SQLException("Error closing the result set when" + action);
			}try {
				
				preparedStatement.close();
			
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + action);
			}
			
			
			
				}
		return Optional.empty();
			
	}
		
	public Optional<User> getUserById(int id) throws SQLException{

			
			String studentQuery = "SELECT  * FROM unidb.student WHERE id = ?";
			String professorQuery = "Select * FROM unidb.professor WHERE id = ?";
			Optional<User> optUser;
			
			optUser = executeIdQuery(id , "student", studentQuery);
				
			if(optUser.isPresent())
					return optUser;
				
			else return executeIdQuery(id, "professor", professorQuery);	
				
				
		}

	private Optional<User> executeIdQuery(int id, String role, String query) throws SQLException {
	
		String action = " finding a user by id";
		ResultSet result = null;
		PreparedStatement preparedStatement = null;
			
			try {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, id);
				result = preparedStatement.executeQuery();
				if (!result.isBeforeFirst()) // no results, credential check failed
					return Optional.empty();
				
				else {
					result.next();
					User user = new User(result.getInt("id"), result.getString("mail"), result.getString("name"), result.getString("surname"), role);
					return Optional.ofNullable(user);
					
				}
				
			} catch(SQLException e) {
				throw new SQLException("Error accessing the DB when" + action);
			}
				finally {
				
				try {
					assert result != null;
					result.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + action);
			}
			
				try {
					preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + action);
			}
				

		}
			
		}
			
	
	
	public Student findStudentById(int studentId) throws SQLException{
		

		String performedAction = " finding student by id";
		
		String query = "SELECT * FROM unidb.student WHERE id = ?";
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Student student = null;
		
		try {
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, studentId);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) // no results
				return null;
			
			while(resultSet.next()) {
		
				
				student = new Student(resultSet.getInt("id"), resultSet.getString("mail"),resultSet.getString("name"), resultSet.getString("surname"),  "student", resultSet.getString("school"), resultSet.getString("degree"));
			}
			
			
		} catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			try {
				   assert resultSet != null;
				resultSet.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		return student;
	}
	
	public Professor findProfessorById(int professorId) throws SQLException{
		

		String performedAction = " finding professor by id";
		
		String query = "SELECT id, mail, name, surname, department FROM unidb.professor WHERE id = ?";
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Professor professor = null;
		
		try {
			
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, professorId);
			
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) // no results
				return null;
			
			while(resultSet.next()) {				
				professor = new Professor(resultSet.getInt("id"), resultSet.getString("mail"),resultSet.getString("name"), resultSet.getString("surname"),  "professor", resultSet.getString("department"));
				
			}
			
			
			
		} catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			try {
				   assert resultSet != null;
				resultSet.close();
				
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		return professor;
	}
	
	
}
