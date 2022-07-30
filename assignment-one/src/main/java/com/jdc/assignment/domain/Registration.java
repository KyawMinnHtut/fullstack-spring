package com.jdc.assignment.domain;

public class Registration {
	private int id;
	private OpenClass openClass;
	private String studentName;
	private String phone;
	private String email;
	private int classId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OpenClass getOpenClass() {
		return openClass;
	}

	public void setOpenClass(OpenClass openClass) {
		this.openClass = openClass;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String student) {
		this.studentName = student;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

}
