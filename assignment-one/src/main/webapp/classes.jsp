<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx"
	crossorigin="anonymous">
</head>
<body>
	<div class="container mt-4">
		<h1>Using IoC Container</h1>
		<h3>Classes for ${course.name}</h3>

		<div>
			<c:url var="addNew" value="/class-edit">
				<c:param name="courseId" value="${course.id}"></c:param>
			</c:url>
			<a class="btn btn-primary" href="${addNew}">Add New Class</a>
		</div>

		<c:choose>
			<c:when test="${empty classes}">
				<div class="alert alert-warning">There is no class for
					${course.name}. Please create new class.</div>
			</c:when>
			<c:otherwise>
				<table class="table table-striped">
					<thead>
						<th>ID</th>
						<th>Course</th>
						<th>Teacher</th>
						<th>Start Date</th>
						<th>Fees</th>
						<th>Duration</th>
						<th>Description</th>
						<th></th>
					</thead>
					<tbody>
						<c:forEach var="c" items="${classes}">
							<tr>
								<td>${c.id}</td>
								<td>${c.course.name}</td>
								<td>${c.teacher}</td>
								<td>${c.startDate}</td>
								<td>${c.course.fees}</td>
								<td>${c.course.duration} Months</td>
								<td>${c.course.description }</td>
								<td><c:url var="classes" value="/registration">
											<c:param name="classId" value="${c.id}"></c:param>
										</c:url> 
										<a href="${classes}">Registered Students</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>