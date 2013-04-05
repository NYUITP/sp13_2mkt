<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@include file="../../resources/include/head.jsp"%>

<script type="text/javascript">

	function load() {
	}
	
	</script>
<div class="container">
	<hr class="space" />

	<%@include file="../../resources/include/header.jsp"%>
	<hr class="space" />

<div class="span11 well">
	<div class="row">
		<div class="pull-left"><img class="img-rounded" src="${investor.image}" width="150" height="150" /></div>
		<div class="span3">
          	<h3><c:out value="${investor.name}"/></h3>
			<span class="badge badge-info"><c:out value="${investor.follower_count}" /> Followers</span>
			<hr class="space" />
			<span class="badge badge-info"><c:out value="${investor.company_count}" /> Companies Invested in</span>
		</div>
	</div>
</div>
<hr class="space" />
<div class="span11 well">
	<div class="row">
		<div class="span11">
          	<h4>About</h4>
			<span><c:out value="${investor.bio}" /></span>
			<span><c:out value="${investor.company_count}" /> Companies Invested in</span>
		</div>
	</div>
</div>
</div>


</body>
</html>
