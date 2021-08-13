package it.polimi.tiw.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Student;
import it.polimi.tiw.utils.MutablePair;

public class ExamRegisterDAO {
	
	
	private Connection connection;

	public ExamRegisterDAO(Connection connection) {
		this.connection = connection;
	}

	public void setGradeByStudentId(int studentId, int examId, int grade) throws SQLException {
		
		String performedAction = " setting student grade in the database";
		
		String queryAddGrade = "UPDATE unidb.exam_register SET grade = ?, state = 'inserted' WHERE exam_id = ? AND student_id = ?";
		
		
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
		
		String queryAddGrade =  "UPDATE unidb.exam_register SET state = ? WHERE exam_id = ? AND student_id = ?";
		
		
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
		
		String query = "SELECT exam_register.student_id, student.mail, student.name, student.surname, student.school, student.degree, exam_register.grade, exam_register.state "
				+ "FROM unidb.exam_register JOIN unidb.student "
				+ "ON exam_register.student_id = student.id "
				+ "WHERE exam_register.exam_id = ? "
				+ "ORDER BY student.surname DESC";
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Student> students = new ArrayList<>();

		
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, examId);
			
			
		try {	
			
			resultSet = preparedStatement.executeQuery();
			
			if(!resultSet.isBeforeFirst())
				return students;
			
			while(resultSet.next()) {
		
				
				Student student = new Student(resultSet.getInt("student_id"), resultSet.getString("mail"),resultSet.getString("name"), resultSet.getString("surname"),  "student", resultSet.getString("school"), resultSet.getString("degree"));
				students.add(student);
			}
			
			
		} catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction +e.getMessage() + " " + examId);
			
		} finally {
			try {
				if(resultSet!=null)
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
		

		String performedAction = " finding students by report id";
		
		String query = "SELECT exam_register.student_id, student.mail, student.name, student.surname, student.school, student.degree, exam_register.grade, exam_register.state "
				+ "FROM unidb.exam_register JOIN unidb.student "
				+ "ON exam_register.student_id = student.id "
				+ "WHERE exam_register.exam_id = ? "
				+ "AND exam_register.id_report = ? "
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
				if(resultSet!=null)
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
		
		String queryAddGrade =  "UPDATE unidb.exam_register SET state = 'published' WHERE state = 'inserted'";
		
		
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
	
	public MutablePair<Integer, String> getExamRegisterByStudentID(int studentId, int examId) throws SQLException {
		

		String performedAction = " finding student's exam grade and state by exam id and student id";
		
		String query = "SELECT grade, state FROM unidb.exam_register WHERE student_id = ? AND exam_id = ? ";
			
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		MutablePair<Integer, String> examRegister = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, studentId);
			preparedStatement.setInt(2, examId);
			
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			examRegister = new MutablePair<>(resultSet.getInt("grade"), resultSet.getString("state"));
		
				
					
			
			
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

		
		String performedAction = " changing student's exam state from 'published'/'refused' to 'recorded' and adding report id by exam id and student id ";
		
		String query = "UPDATE unidb.exam_register "
				+ "( SET grade = 'postponed' "
				+ "WHERE exam_id = ?  AND state = 'refused') "
				+ "AND "
				+ "(SET state = 'recorded' AND report_id = ? "
				+ "WHERE exam_id = ? "
				+ "AND (state = 'published' OR state = 'refused') )";
			
		PreparedStatement preparedStatement = null;
		
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, examId);
			preparedStatement.setInt(2, reportId);
			preparedStatement.setInt(3, examId);
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
