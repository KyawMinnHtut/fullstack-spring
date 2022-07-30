package com.jdc.assignment.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdc.assignment.domain.OpenClass;
import com.jdc.assignment.domain.Registration;
import com.jdc.assignment.model.CourseModel;
import com.jdc.assignment.model.OpenClassModel;
import com.jdc.assignment.model.RegistrationModel;

@WebServlet(urlPatterns = {
		"/registration",
		"/registration-edit"
})
public class RegistrationServlet extends AbstractBeanFactoryServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		var classId = req.getParameter("classId");

		// Find class
		var registrationModel = getBean("openClassModel", OpenClassModel.class);
		var openClass = registrationModel.findbyId(Integer.parseInt(classId));

		req.setAttribute("classes", openClass);

		var page = switch (req.getServletPath()) {
		case "/registration" -> {
			var model = getBean("registrationModel", RegistrationModel.class);
			req.setAttribute("students", model.findByClass(Integer.parseInt(classId)));
			yield "registration";
		}
		default -> "registration-edit";
		};

		getServletContext().getRequestDispatcher("/%s.jsp".formatted(page)).forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// get requeset parameter
		var classId = req.getParameter("classId");
		var student = req.getParameter("studentName");
		var email = req.getParameter("email");
		var phone = req.getParameter("phone");
		// create course object

		var reg = new Registration();
		reg.setClassId(Integer.parseInt(classId));
		reg.setStudentName(student);
		reg.setEmail(email);
		reg.setPhone(phone);

		// save to db
		getBean("registrationModel", RegistrationModel.class).save(reg);
		;

		// redirect to top page
		resp.sendRedirect(String.format("/registration?classId=%s", classId));
	}

}
