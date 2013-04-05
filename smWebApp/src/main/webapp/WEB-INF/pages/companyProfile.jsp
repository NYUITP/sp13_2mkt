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
			<div class="pull-left">
				<img class="img-rounded" src="${company.logo_url}" width="150"
					height="150" />
			</div>
			<div class="span3">
				<h3>
					<c:out value="${company.name}" />
				</h3>
				<span class="badge badge-info"><c:out
						value="${company.follower_count}" /> Followers</span>

				<hr class="space" />

				<span class="badge badge-info"><c:out
						value="$${company.total_funding}M" /> Total Funding</span>
				<hr class="space" />
				<div>
					<a id="profileAnglelistInIcon" target="_blank"
						href="${company.angellist_url}"> <img class="img-rounded"
						src="resources/img/pro-anglelist-icon.png" width="100" height="100"></a>
					<a id="profileTwitterIcon" target="_blank"
						href="${company.twitter_url}"> <img class="img-rounded"
						src="resources/img/pro-twitter-icon.png" width="30" height="30"></a>
				</div>

			</div>
		</div>
	</div>
	<hr class="space" />

	<div class="span11 well">
		<div class="row">
			<div class="span11">
				<h4>About</h4>
				<span><c:out value="${company.product_desc}" /></span>
			</div>
		</div>
	</div>
</div>
</body>
</html>
