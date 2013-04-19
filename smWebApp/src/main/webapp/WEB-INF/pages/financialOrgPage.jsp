<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@include file="../../resources/include/head.jsp"%>

<script type="text/javascript">
	function load() {
		showValueFollower(document.getElementById("followerRange").value);
		showValueCompany(document.getElementById("companyRange").value);
		showValueROI(document.getElementById("roiRange").value);
	}
	function showValueFollower(newValue) {
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

	function showValueROI(newValue) {
		if (newValue == 1) {
			document.getElementById("roi-value").innerHTML = "Not Important";
			document.getElementById('roiImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("roi-value").innerHTML = "A Little Important";
			document.getElementById('roiImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("roi-value").innerHTML = "Moderately Important";
			document.getElementById('roiImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("roi-value").innerHTML = "Important";
			document.getElementById('roiImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("roi-value").innerHTML = "Very Important";
			document.getElementById('roiImpLevel').value = 5;
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

				<input id="followerRange" type="range" min="1" max="5" step="1"
					value="${followerLevel}" onchange="showValueFollower(this.value)" />

				<span id="follower-value" class="slider-value">Moderately
					Important</span>

			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of Companies Invested In</strong>
				</div>
				<hr class="space" />

				<input id="companyRange" type="range" min="1" max="5" step="1"
					value="${companyLevel}" onchange="showValueCompany(this.value)" />

				<span id="company-value" class="slider-value">Moderately
					Important</span>
			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of ROI</strong>
				</div>
				<hr class="space" />

				<input id="roiRange" type="range" min="1" max="5" step="1"
					value="${roiLevel}" onchange="showValueROI(this.value)" /> <span
					id="roi-value" class="slider-value">Moderately Important</span>
			</div>

			<hr class="space" />
			
			<form action="finOrgRankingByFC_CC_ROI" method="post">
				<input type='hidden' id='companyImpLevel' name='companyImpLevel'
					value='3' /> <input type='hidden' id='followersImpLevel'
					name='followersImpLevel' value='3' /> <input type='hidden'
					id='roiImpLevel' name='roiImpLevel' value='3' /> <input
					type="submit" value="Update">
			</form>

			<div class="side-block">
				<div>
					<strong>Filter by Companies Invested In</strong>
				</div>
				<hr class="space" />
				<ul class="clear-ul">

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;0-50
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;51-100
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;101-150
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;150+
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

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;San
						Francisco <a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;New
						York <a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;Boston
						<a class="small-text pull-right" href="#">only</a></li>

					<li class="mts"><input type="checkbox" checked="checked">&nbsp;London
						<a class="small-text pull-right" href="#">only</a></li>
				</ul>
				<hr class="space" />
				<a href="#" class="btn">Update</a>
			</div>
		</div>

		<div class="span9">
			<h2>Financial Organizations</h2>
			<hr>
			<c:forEach items="${finOrgs}" var="finOrg">
				<div class="search-result">
					<div class="pull-left search-pic">
						<img alt="${finOrg.name}" src="${finOrg.logo_url}" width="50"
							height="50" />
					</div>

					<div class="pull-left search-content">
						<div>
							<h4 class="pull-left">
								<a
									href=financialOrgProfile?permalink=${finOrg.permalink}><c:out
										value="${finOrg.name}" /></a>
							</h4>
						</div>
						<div>
							<c:forEach items="${finOrg.locations}" var="location">
								<span class="pull-left muted"> <small>${location.name}
								</small></span>
							</c:forEach>
						</div>
					</div>

					<div class="search-stats pull-right">
						<div>
							<span class="large-number"><c:out
									value="${finOrg.follower_count}" /></span> Followers
						</div>
						<div>
							<span class="large-number"><c:out
									value="${finOrg.company_count}" /></span> Companies Invested in
						</div>
						<div>
							<span class="large-number"><c:out
									value="${finOrg.average_roi}" /></span> Average ROI
						</div>
					</div>
					<div class="clearfix"></div>
					<hr class="space" />
				</div>
				<hr>
			</c:forEach>
		</div>
	</div>
</div>
</body>
</html>
