package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.polimi.tiw.beans.*;


public class CourseDAO {
	
	private Connection connection;

	public CourseDAO(Connection connection) {
		this.connection = connection;
	}
	
	
	public List<Course> getCoursesByStudentId(int studentId) throws SQLException{
		
		String performedAction = " finding courses by student id";
		
		String query = "SELECT id, name FROM unidb.study_plan JOIN unidb.course ON study_plan.course_id = course.id WHERE student_id = ?";
		
		return executeQuery(performedAction, query, studentId);
		
	}
	
	public List<Course> getCoursesByProfessorId(int professorId) throws SQLException{
		
		String performedAction = " finding courses by professor id";
		
		String query = "SELECT id, name FROM unidb.course WHERE professor_id = ? ORDER BY name DESC";
		
		return executeQuery(performedAction, query, professorId);
		
	}
	
	
	private List<Course> executeQuery(String performedAction, String query, int id) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Course> courses = new ArrayList<>();
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				Course course = new Course(resultSet.getInt("id"), resultSet.getString("name") );
				courses.add(course);
			}
			
			
		} catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		} finally {
			try {
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
		
		return courses;
		
	}
	
	public boolean isCourseIdValid(int courseId) throws SQLException {
		
			String performedAction = " finding course by id";
			
			String query = "SELECT * FROM unidb.course WHERE id = ?";
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			try {
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, courseId);
				
				resultSet = preparedStatement.executeQuery();
				
				if(resultSet.isBeforeFirst())
					return true;
				
				
				
			} catch(SQLException e) {
				throw new SQLException("Error accessing the DB when" + performedAction);
				
			} finally {
				try {
					resultSet.close();
				}catch (SQLException e) {
					throw new SQLException("Error closing the result set when" + performedAction);
				}
				try {
					preparedStatement.close();
				}catch (SQLException e) {
					throw new SQLException("Error closing the statement when" + performedAction);
				}
			}
			
			return false;
		 
	 }
	}

	

