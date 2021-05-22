package it.polimi.tiw.dao;

import java.sql.*;
import java.util.AbstractMap.SimpleImmutableEntry;
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
		
		String queryAddGrade = "UPDATE exam_register SET grade = ?, state = 'inserted' WHERE exam_id = ? AND student_id = ?";
		
		
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddGrade);
			preparedStatementAddUser.setInt(1, grade);
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
	
	public List<Student> getStudentsByReportId(int examId, int reportId) throws SQLException {
		

		String performedAction = " finding students by exam id";
		
		String query = "SELECT student.id, student.mail, student.name, student.surname, student.school, student.degree, exam_register.grade, exam_register.state"
				+ "FROM exam_register JOIN student "
				+ "ON student.id = exam_register.student_id"
				+ "WHERE exam_register.id = ? "
				+ "AND id_report = ?"
				+ "ORDER BY student.surname DESC";
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Student> students = new ArrayList<>();
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, examId);
			preparedStatement.setInt(2, reportId);
			
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
	
	public void publishGradeByExamID(int examId) throws SQLException {
		
		String performedAction = " changing all students grade state in the database from insterted to published";
		
		String queryAddGrade =  "UPDATE exam_register SET state = 'published' WHERE state = 'inserted'";
		
		
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddGrade);
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
	
	public SimpleImmutableEntry<Integer, String> getExamRegisterByStudentID(int studentId, int examId) throws SQLException {
		

		String performedAction = " finding student's exam grade and state by exam id and student id";
		
		String query = "SELECT grade, state FROM exam_register WHERE student_id = ? AND exam_id = ? ";
			
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		SimpleImmutableEntry<Integer, String> examRegister = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, examId);
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
		
				examRegister = new SimpleImmutableEntry<>(resultSet.getInt("grade"), resultSet.getString("state"));
					
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
		
		return examRegister;
		
		
	}
		
	public void reportGradeByExamID(int reportId, int examId) throws SQLException {
		
		String performedAction = " changing student's exam state from 'published'/'refused' to 'recorded' and adding report id by exam id and student id";
		
		String query = "UPDATE exam_register "
				+ "SET state = 'recorded' AND report_id = ? "
				+ "WHERE exam_id = ? "
				+ "AND (state = 'published' OR state = 'refused')";
			
			
		
		PreparedStatement preparedStatement = null;
		
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		}finally {
			
			try {
				
				preparedStatement.close();
				
			}catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
				
			}
		}
		
		
		
	}
	
}
