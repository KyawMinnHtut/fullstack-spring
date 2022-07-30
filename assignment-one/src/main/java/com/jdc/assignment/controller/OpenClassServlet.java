package com.jdc.assignment.controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdc.assignment.domain.Course;
import com.jdc.assignment.domain.OpenClass;
import com.jdc.assignment.model.CourseModel;
import com.jdc.assignment.model.OpenClassModel;
import com.jdc.assignment.model.impl.CourseModelImpl;

@WebServlet(urlPatterns = { "/classes", "/class-edit" })
public class OpenClassServlet extends AbstractBeanFactoryServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		var courseId = req.getParameter("courseId");

		// Find course
		var courseModel = getBean("courseModel", CourseModel.class);
		var course = courseModel.findById(Integer.parseInt(courseId));

		req.setAttribute("course", course);

		var page = switch (req.getServletPath()) {
		case "/classes" -> {
			var model = getBean("openClassModel", OpenClassModel.class);
			req.setAttribute("classes", model.findByCourse(Integer.parseInt(courseId)));
			yield "classes";
		}
		default -> "class-edit";
		};

		getServletContext().getRequestDispatcher("/%s.jsp".formatted(page)).forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// get requeset parameter
		var courseId = req.getParameter("courseId");
		var startDate = req.getParameter("start_date");
		var teacher = req.getParameter("teacher");

		// create course object
		var openClass = new OpenClass();
		openClass.setCourseId(Integer.parseInt(courseId));
//		openClass.setStartDate(startDate);
		openClass.setStartDate(Date.valueOf(startDate).toLocalDate());
		openClass.setTeacher(teacher);

		// save to db
		getBean("openClassModel", OpenClassModel.class).create(openClass);
		;

		// redirect to top page
		resp.sendRedirect(String.format("/classes?courseId=%s", courseId));
	}

}
