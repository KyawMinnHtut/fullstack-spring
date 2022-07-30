package com.jdc.assignment.model.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jdc.assignment.domain.Course;
import com.jdc.assignment.domain.OpenClass;
import com.jdc.assignment.domain.Registration;
import com.jdc.assignment.model.RegistrationModel;

public class RegistrationModelImpl implements RegistrationModel {
	
	private static final String SELECT_SQL = "select reg.open_class_id classId, oc.start_date, oc.teacher, reg.id, reg.student, reg.phone, reg.email from open_class oc join registration reg on oc.id=reg.open_class_id where oc.id=?;";
	private static final String INSERT = "insert into registration(open_class_id, student, phone, email) values(?, ?, ?, ?)";
	private DataSource dataSource;

	public RegistrationModelImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public List<Registration> findByClass(int classId) {
		var list = new ArrayList<Registration>();
		try(var conn = dataSource.getConnection();
				var stmt = conn.prepareStatement(SELECT_SQL)){
			stmt.setInt(1, classId);
			var rs = stmt.executeQuery();
			while (rs.next()) {

				var oc = new OpenClass();
				oc.setId(rs.getInt("classId"));
				oc.setTeacher(rs.getString("teacher"));
				oc.setStartDate(rs.getDate("start_date").toLocalDate());
				
				var stu = new Registration();
				stu.setOpenClass(oc);
				stu.setId(rs.getInt("id"));
				stu.setClassId(rs.getInt("classId"));
				stu.setStudentName(rs.getString("student"));
				stu.setEmail(rs.getString("email"));
				stu.setPhone(rs.getString("phone"));
				
				list.add(stu);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;	}

	@Override
	public void save(Registration registration) {
		try(var conn = dataSource.getConnection();
				var stmt = conn.prepareStatement(INSERT)){
			
			stmt.setInt(1, registration.getClassId());
			stmt.setString(2, registration.getStudentName());
			stmt.setString(3, registration.getPhone());
			stmt.setString(4, registration.getEmail());
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	}

}
