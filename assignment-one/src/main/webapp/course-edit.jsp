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
	<h3>Add New Course</h3>
	<div class="row">
		<div class="col-4">
			<c:url var="save" value="/courses"></c:url>
			<form action="${save}" method="post">
			<div class="mb-3">
			<label class="form-label" for="">Name</label>
			<input type="text" name="name" placeholder="Enter Course Name" required="required" class="form-control"/>
			</div>
			<div class="mb-3">
			<label class="form-label" for="">Duration</label>
			<input type="number" name="duration" placeholder="Enter Course Duration" required="required" class="form-control"/>
			</div>
			<div class="mb-3">
			<label class="form-label" for="">Fees</label>
			<input type="number" name="fees" placeholder="Enter Course Fees" required="required" class="form-control"/>
			</div>
			<div class="mb-3">
			<label class="form-label" for="">Description</label>
			<textarea rows="4" cols="40" name="description" class="from-control"></textarea>
			</div>
			<input type="submit" value="Save Course" class="btn btn-primary"/>
			</form>
		</div>
	</div>
</div>
</body>
</html>