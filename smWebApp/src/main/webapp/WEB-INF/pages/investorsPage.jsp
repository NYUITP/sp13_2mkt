<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@include file="../../resources/include/head.jsp"%>

<script type="text/javascript">

	function load() {
		showValueInvestor(document.getElementById("followerRange").value);
		showValueCompany(document.getElementById("companyRange").value);
	}
	function showValueInvestor(newValue) {
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

				<input id="followerRange" type="range" min="1" max="5" step="1" value="${followerLevel}"
					onchange="showValueInvestor(this.value)" />

				<span id="follower-value" class="slider-value">Moderately Important</span>
				
			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of Companies Invested In</strong>
				</div>
				<hr class="space" />

				<input id="companyRange" type="range" min="1" max="5" step="1" value="${companyLevel}"
					onchange="showValueCompany(this.value)" />

				<span id="company-value" class="slider-value">Moderately Important</span>
				
			</div>
			
			
			<hr class="space" />
				<form action="investorRanking" method="post">
					<input type='hidden' id='companyImpLevel' name='companyImpLevel' value='3'/>
					<input type='hidden' id='followersImpLevel' name='followersImpLevel' value='3'/>
					<input type="submit" value="Update">
				</form>
			
			<div class="side-block">
				<div>
					<strong>Filter by Companies Invested In</strong>
				</div>
				<hr class="space" />
				<ul class="clear-ul">

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;0-50</input>
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;51-100</input>
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;101-150</input>
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;150+</input>
						<a class="small-text pull-right" href="#">only</a></li>
				</ul>

				<hr class="space" />
				<a href="#" class="btn">Update</a>

			</div>

			<hr class="space" />
			
			<div class="side-block">
				<div>
					<strong>Filter by Location</strong>
				</div>
				<hr class="space" />
				<ul class="clear-ul">

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;San Francisco</input>
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;New York</input>
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;Boston</input>
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;London</input>
						<a class="small-text pull-right" href="#">only</a></li>
				</ul>

				<hr class="space" />
				<a href="#" class="btn">Update</a>

			</div>

		</div>


		<div class="span9">

			<h1>Investors</h1>
			<hr>
			<c:forEach items="${investors}" var="investor">
				<div class="search-result">
					<div class="pull-left search-pic">
						<img src="${investor.image}" width="50" height="50" />
					</div>

					<div class="pull-left search-content">
						<div>
							<h4 class="pull-left">
								<a href="investorProfile"><c:out value="${investor.name}" />
							</h4>
							</a>
						</div>
						<div>
							<c:forEach items="${investor.locations}" var="location">
							<span class="pull-left" >${location.location_name}</span>
							</c:forEach>
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

					<!-- <c:out value="${investor.bio}" /> -->
					<hr class="space" />
				</div>
			</c:forEach>
		</div>
	</div>
</div>
</body>
</html>
