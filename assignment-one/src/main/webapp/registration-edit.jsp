<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
</head>
<body>
<div class="container mt-4">
	<h1>Using IoC Container</h1>
	<h3>Add Students for (${classes.startDate}) Class</h3>
	<div class="row">
		<div class="col-4">
			<c:url var="save" value="/registration">
				<c:param name="classId" value="${classes.id}"></c:param>
			</c:url>
			<form action="${save}" method="post">

			<div class="mb-3">
			<label class="form-label" for="">Name</label>
			<input type="text" name="studentName" placeholder="Enter Student Name" required="required" class="form-control"/>
			</div>
			<div class="mb-3">
			<label class="form-label" for="">Email</label>
			<input type="text" name="email" placeholder="Enter Student Email" required="required" class="form-control"/>
			</div>
			<div class="mb-3">
			<label class="form-label" for="">Phone</label>
			<input type="number" name="phone" placeholder="Enter Student Phone" required="required" class="form-control"/>
			</div>
			<input type="submit" value="Save Class" class="btn btn-primary"/>
			</form>
		</div>
	</div>
</div>
</body>
</html>