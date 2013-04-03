<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@include file="../../resources/include/head.jsp"%>

<script type="text/javascript">

	function showValue(newValue) {
		if (newValue == 1) {
			document.getElementById("follower-value").innerHTML = "Not Important";
			document.getElementById('followersImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("follower-value").innerHTML = "A Little Important";
			document.getElementById('followersImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("follower-value").innerHTML = "Moderately Important";
			document.getElementById('followersImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("follower-value").innerHTML = "Important";
			document.getElementById('followersImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("follower-value").innerHTML = "Very Important";
			document.getElementById('followersImpLevel').value = 5;
		}

	}
</script>

<script type="text/javascript">

	function showValueCompany(newValue) {
		if (newValue == 1) {
			document.getElementById("company-value").innerHTML = "Not Important";
			document.getElementById('companyImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("company-value").innerHTML = "A Little Important";
			document.getElementById('companyImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("company-value").innerHTML = "Moderately Important";
			document.getElementById('companyImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("company-value").innerHTML = "Important";
			document.getElementById('companyImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("company-value").innerHTML = "Very Important";
			document.getElementById('companyImpLevel').value = 5;
		}

	}
</script>


<div class="container">
	<hr class="space" />

	<%@include file="../../resources/include/header.jsp"%>

	<hr class="space" />
	<div class="row">

		<div class="span3">
			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of Followers</strong>
				</div>
				<hr class="space" />

				<input type="range" min="1" max="5" step="1" value="3"
					onchange="showValue(this.value)" />

				<!-- <span class="pull-left">Not</span> -->
				<span id="follower-value" class="slider-value">Moderately Important</span>

				<hr class="space" />
				<form action="investorRanking" method="post">
					<input type='hidden' id='followersImpLevel' name='followersImpLevel' value='3'/>
					<input type="submit" value="Update">
				</form>
			</div>

			<hr class="space" />
			
			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of Companies Invested In</strong>
				</div>
				<hr class="space" />

				<input type="range" min="1" max="5" step="1" value="3"
					onchange="showValueCompany(this.value)" />

				<!-- <span class="pull-left">Not</span> -->
				<span id="company-value" class="slider-value">Moderately Important</span>

				<hr class="space" />
				<form action="investorRanking" method="post">
					<input type='hidden' id='companyImpLevel' name='companyImpLevel' value='3'/>
					<input type="submit" value="Update">
				</form>
			</div>


		</div>


		<div class="span9">

			<h1>Investors</h1>
			<hr>
			<c:forEach items="${investors}" var="investor">
				<div class="search-result">
					<div class="pull-left search-pic">
						<img src="resources/img/user-icon.png" width="50" height="50" />
					</div>

					<div class="pull-left search-content">
						<div>
							<h4 class="pull-left">
								<a href="investorProfile"><c:out value="${investor.name}" />
							</h4>
							</a>
						</div>
						<div>
							<a class="pull-left" href="#">Website</a>
						</div>
					</div>

					<div class="search-stats pull-right">
						<div>
							<span class="large-number"><c:out
									value="${investor.follower_count}" /></span> Followers
						</div>
						<div>
							<span class="large-number"><c:out
									value="${investor.company_count}" /></span> Companies Invested in
						</div>
					</div>

					<div class="clearfix"></div>

					<c:out value="${investor.bio}" />
					<hr class="space" />
				</div>
			</c:forEach>
		</div>
	</div>
</div>
</body>
</html>
