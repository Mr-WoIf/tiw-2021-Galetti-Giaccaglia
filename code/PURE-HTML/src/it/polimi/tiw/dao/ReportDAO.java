package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import it.polimi.tiw.beans.Report;
import it.polimi.tiw.beans.Student;

public class ReportDAO {
	
	private Connection connection;

	public ReportDAO(Connection connection) {
		this.connection = connection;
	}
	
	public Report createReport(int examId) throws SQLException {

		
		Date date = new Date();

		Object sqlDatetime = new Timestamp(date.getTime());

		String performedAction = "creating report in the database";
		
		String queryCreateReport = "INSERT INTO unidb.report (id, exam_id,date_recorded) VALUES (?, ?, ?)";
		
		int reportId = new Random(System.nanoTime()).nextInt();
		
		Report report = null;
		
		List<Student> students = null;
		
		 Map<Integer, Integer> studentsGrades = new HashMap<>();
		
		
		PreparedStatement preparedStatement = null;	
		ExamRegisterDAO registerDAO = new ExamRegisterDAO(connection);
		
		//Delimit the transaction explicitly, not to leave the db in inconsistent state
		connection.setAutoCommit(false);
		
		try {
			
			preparedStatement = connection.prepareStatement(queryCreateReport);
			preparedStatement.setInt(1, reportId);
			preparedStatement.setInt(2, examId);
			preparedStatement.setObject(3, sqlDatetime); 
			
			registerDAO.reportGradeByExamID(examId, reportId);
			
			connection.commit();
			
		}catch(SQLException e) {
			
			connection.rollback();
			
			throw new SQLException("Error accessing the DB when" + performedAction);
			
		}finally {
			
			try {
				
				preparedStatement.close();
				
			}catch (Exception e) {
				
				throw new SQLException("Error closing the statement when" + performedAction);
				
			}
		}
		
		try {
			
		students = registerDAO.getStudentsByReportId(examId, reportId);
		
		}catch(SQLException e) {
			
			throw new SQLException(e);
		}
		
		
		int studentGrade;
		
		for(Student student : students) {
			
			
			try {
				
				studentGrade = registerDAO.getExamRegisterByStudentID(student.getId(), examId).getKey();
				studentsGrades.put(student.getId(), studentGrade);
				
				}catch(SQLException e) {
					
					throw new SQLException(e);
				}		
				
		}
		
		report = new Report(students, reportId, date, studentsGrades);
		
		return report;
		
	}

	

}
