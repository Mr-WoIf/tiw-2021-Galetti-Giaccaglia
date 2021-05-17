package it.polimi.tiw.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Student;

public class ExamRegisterDAO {
	
	
	private Connection connection;

	public ExamRegisterDAO(Connection connection) {
		this.connection = connection;
	}
	

	public void setGradeByStudentId(int studentId, int examId, int grade) throws SQLException {
		
		String performedAction = " setting student grade in the database";
		
		String queryAddGrade = "UPDATE  SET balance = balance - ? WHERE id = ?";
		
		
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddGrade);
			preparedStatementAddUser.setInt(1, studentId);
			preparedStatementAddUser.setInt(2, examId);
			preparedStatementAddUser.setInt(3, grade);
			preparedStatementAddUser.executeUpdate();
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		}finally {
			
			try {
				preparedStatementAddUser.close();
			}catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
				
			}
		}
	}
	
	
	public void setGradeStateByExamID(int studentId, int examId, String gradeState) throws SQLException {
		
		String performedAction = " setting student grade state in the database";
		
		String queryAddGrade =  "UPDATE exam_register SET state = ? WHERE exam_id = ? AND student_id = ?";
		
		
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddGrade);
			preparedStatementAddUser.setString(1, gradeState);
			preparedStatementAddUser.setInt(2, examId);
			preparedStatementAddUser.setInt(3, studentId);
			preparedStatementAddUser.executeUpdate();
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		}finally {
			
			try {
				
				preparedStatementAddUser.close();
				
			}catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
				
			}
		}
		
	}
	
	public List<Student> getStudentsByExamId(int examId) throws SQLException {
		

		String performedAction = " finding students by exam id";
		
		String query = "SELECT student.id, student.mail, student.name, student.surname, student.school, student.degree, exam_register.grade, exam_register.state "
				+ "FROM exam_register JOIN student "
				+ "ON student.id = exam_register.student_id"
				+ "WHERE exam_register.id = ? "
				+ "ORDER BY student.surname DESC";
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Student> students = new ArrayList<>();
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, examId);
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
		
				
				Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("mail"),resultSet.getString("name"), resultSet.getString("surname"),  "student", resultSet.getString("school"), resultSet.getString("degree"));
				students.add(student);
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
		
		return students;
	}
	
	
	
}
