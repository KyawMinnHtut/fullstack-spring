<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Leaves | Home</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-A3rJD856KowSb7dwlZdYEkO39Gagi7vIsF0jrRAoQmDKKtQBHUuLZ9AsSv4jD4Xa" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css">

<c:url var="commonCss" value="/resources/application.css"></c:url>
<link rel="stylesheet" href="${commonCss}" />

</head>
<body>

	<c:import url="/jsp/include/navbar.jsp">
		<c:param name="view" value="leaves"></c:param>
	</c:import>

	<div class="container">
		
		<h3 class="my-4">Leaves</h3>
		
		<!-- Search Form -->
		<form class="row mb-4">
			<div class="col-auto">
				<label class="form-label">Date From</label>
				<input type="date" name="from" class="form-control" value="${ param.from }"/>
			</div>
			<div class="col-auto">
				<label class="form-label">Date To</label>
				<input type="date" name="to" class="form-control" value="${ param.to }" />
			</div>
			<div class="col btn-wrapper">
				<button class="btn btn-outline-success me-2"><i class="bi bi-search"></i> Search</button>
			</div>
		</form>		
		
		<!-- Search Result -->
		<table class="table table-hover">
	
	<thead>
		<tr>
			<th>Teacher</th>
			<th>Apply Date</th>
			<th>Leave Start</th>
			<th>Leave Days</th>
			<th>Reason</th>
		</tr>
	</thead>
	
	<tbody>
		<c:forEach items="${ list }" var="item">
			<tr>
				<td>${ item.teacher }</td>
				<td>${ item.applyDate }</td>
				<td>${ item.startDate }</td>
				<td>${ item.days } Days</td>
				<td>${ item.reason }</td>
			</tr>
		</c:forEach>
	</tbody>

</table>
	
	</div>
</body>
</html>