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

		String performedAction1 = "creating report in the database";
	//	String performedAction2 = " changing student's exam state from 'published'/'refused' to 'recorded' and adding report id by exam id and student id ";
		
		String queryCreateReport = "INSERT INTO unidb.report (id, exam_id,date_recorded) VALUES (?, ?, ?)";
		
		String queryChangeState = "UPDATE unidb.exam_register SET grade = '1' WHERE exam_id = ? AND state = 'refused'; "
				+ "UPDATE unidb.exam_register SET state = 'recorded' WHERE exam_id = ? AND (state = 'published' OR state = 'refused'); "
				+ "UPDATE unidb.exam_register SET id_report = ? WHERE exam_id = ? AND state = 'recorded';";
		
		
		Random rand = new Random(System.nanoTime());
		int reportId = rand.nextInt(2000-1000) + 1000;
		
		Report report = null;
		
		List<Student> students = null;
		
		Map<Integer, Integer> studentsGrades = new HashMap<>();
		
		
		PreparedStatement preparedStatementReport = null;
		PreparedStatement preparedStatementUpdateState = null;
		ExamRegisterDAO registerDAO = new ExamRegisterDAO(connection);
		
		
		try {
			
			//Delimit the transaction explicitly, not to leave the db in inconsistent state
			connection.setAutoCommit(false);
			
			preparedStatementReport = connection.prepareStatement(queryCreateReport);
			preparedStatementUpdateState = connection.prepareStatement(queryChangeState);
			
			preparedStatementReport.setInt(1, reportId);
			preparedStatementReport.setInt(2, examId);
			preparedStatementReport.setObject(3, sqlDatetime); 
			preparedStatementReport.executeUpdate();
			
			preparedStatementUpdateState.setInt(1, examId);
			preparedStatementUpdateState.setInt(2, examId);
			preparedStatementUpdateState.setInt(3, reportId);
			preparedStatementUpdateState.setInt(4, examId);
			preparedStatementUpdateState.executeUpdate();
				
			connection.commit();
			
		}catch(SQLException e) {
			
			connection.rollback();
		//	System.out.println(e.toString()); //for debug
			
			throw new SQLException("Error accessing the DB when " + performedAction1);
			
		}finally {
			
			connection.setAutoCommit(true);
			try {
				
				preparedStatementReport.close();
				preparedStatementUpdateState.close();
				
			}catch (Exception e) {
				
				throw new SQLException("Error closing the statement when " + e.toString());
				
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
				
				studentGrade = registerDAO.getExamRegisterByStudentID(student.getId(), examId).getLeft();
				studentsGrades.put(student.getId(), studentGrade);
				
				}catch(SQLException e) {
					
					throw new SQLException(e);
				}		
				
		}
		
		report = new Report(students, reportId, date, studentsGrades);
		
		return report;
		
	}

	

}
