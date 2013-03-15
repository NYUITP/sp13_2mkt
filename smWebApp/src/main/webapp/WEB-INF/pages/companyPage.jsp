<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Companies</h1>

	<table style="border: 1px solid; width: 500px; text-align: center">
		<thead style="background: #fcf">
			<tr>
				<th>Name</th>
				<th>Number_Of_Followers</th>
				<th>Total_Funding($million)</th>
				<th>Product_Description</th>
				<th colspan="3"></th>
			</tr>
		</thead>
		<tbody>
			<%
				System.out.println("List: " + request.getParameter("companies"));
			%>

			<c:forEach items="${companies}" var="company">
				<tr>
					<td><c:out value="${company.name}" /></td>
					<td><c:out value="${company.follower_count}" /></td>
					<td><c:out value="${company.total_funding}" /></td>
					<td><c:out value="${company.product_desc}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>