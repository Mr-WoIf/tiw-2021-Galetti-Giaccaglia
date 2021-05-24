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

public class ExamDAO {
	
	private Connection connection;

	public ExamDAO(Connection connection) {
		this.connection = connection;
	}
	
	 public List<Exam> getExamsByCourseId(int courseId) throws SQLException{
		 
		 
			String performedAction = " finding exams by course id";
			
			String query = "SELECT date, id  FROM unidb.exam WHERE course_id = ? ORDER BY date DESC";
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			List<Exam> exams = new ArrayList<>();
			
			try {
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, courseId);
				
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()) {
			
					Exam exam = new Exam(new Date(resultSet.getDate("date").getTime()), resultSet.getInt("id"), courseId);
					exams.add(exam);
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
			
			
			return exams;
		 
	 }
	 
	 public List<Exam> getSubscribedExamsByStudentID(int studentId, int courseId) throws SQLException {
		 
			String performedAction = " error while determining student subscription to exam";
			
			String query = "SELECT * FROM unidb.exam_register WHERE course_id = ? AND student_id = ?";
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			List<Exam> exams = new ArrayList<>();
			
	try 	{
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, courseId);
				preparedStatement.setInt(2, studentId);
				
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()) {
			
					Exam exam = new Exam(new Date(resultSet.getDate("name").getTime()), resultSet.getInt("id"), resultSet.getInt("course_id"));
					exams.add(exam);
				}
				
				
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
	
	
		return exams;
			
			
	 }
	 
	 public Optional<Exam> getExamById(int examId) throws SQLException {
		 
		 String performedAction = " finding exam by id";
			
			String query = "SELECT date, course_id  FROM unidb.exam WHERE id = ?";
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			Exam exam = null;
			
			try {
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, examId);
				
				resultSet = preparedStatement.executeQuery();
				
				if(!resultSet.isBeforeFirst())
					return Optional.ofNullable(exam);
				
				exam = new Exam(new Date(resultSet.getDate("date").getTime()), examId, resultSet.getInt("course_id"));
				
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
			
			return Optional.ofNullable(exam);
		 
		 
	 }
	 

}
