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
		<h3>Students for (${classes.startDate }) Class by Teacher - ${classes.teacher}</h3>

		<div>
			<c:url var="addNew" value="/registration-edit">
				<c:param name="classId" value="${classes.id}"></c:param>
			</c:url>
			<a class="btn btn-primary" href="${addNew}">Add New Student</a>
		</div>

		<c:choose>
			<c:when test="${empty students}">
				<div class="alert alert-warning">There is no students for
					this class. Please register the students.</div>
			</c:when>
			<c:otherwise>
				<table class="table table-striped">
					<thead>
						<th>ID</th>
						<th>Name</th>
						<th>Email</th>
						<th>Phone</th>

					</thead>
					<tbody>
						<c:forEach var="stu" items="${students}">
							<tr>
								<td>${stu.id}</td>
								<td>${stu.studentName}</td>
								<td>${stu.email}</td>
								<td>${stu.phone}</td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>