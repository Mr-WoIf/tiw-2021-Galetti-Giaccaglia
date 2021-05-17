package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.polimi.tiw.beans.*;

public class ExamDAO {
	
	private Connection connection;

	public ExamDAO(Connection connection) {
		this.connection = connection;
	}
	
	 public List<Exam> getExamsByCourseId(int courseId) throws SQLException{
		 
		 
			String performedAction = " finding exams by course id";
			
			String query = "SELECT date, id  FROM exam WHERE course_id = ? ORDER BY date DESC";
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			List<Exam> exams = new ArrayList<>();
			
			try {
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, courseId);
				
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()) {
			
					Exam exam = new Exam(new Date(resultSet.getDate("name").getTime()), resultSet.getInt("id"));
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
	 
	 public boolean getSubscribedExamByStudentID(int studentid, int examid) throws SQLException {
		 
			String performedAction = " error while determining student subscription to exam";
			
			String query = "SELECT * FROM exam_register WHERE exam_id = ? AND student_id = ?";
			
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			
			try {
				
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, examid);
				preparedStatement.setInt(2, studentid);
				
				resultSet = preparedStatement.executeQuery();
				
				return resultSet.isBeforeFirst() ? true : false;
				
				
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
			
			
	 }
	 

}
