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
	<h1>Investors</h1>

	<table style="border: 1px solid; width: 500px; text-align: center">
		<thead style="background: #fcf">
			<tr>
				<th>Name</th>
				<th>Bio</th>
				<th>Number_Of_Followers</th>
				<th colspan="3"></th>
			</tr>
		</thead>
		<tbody>
			<%
				System.out.println("List: " + request.getParameter("investors"));
			%>

			<c:forEach items="${investors}" var="investor">
				<tr>
					<td><c:out value="${investor.name}" /></td>
					<td><c:out value="${investor.bio}" /></td>
					<td><c:out value="${investor.follower_count}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>