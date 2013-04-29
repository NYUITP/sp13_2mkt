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
		<div class="span11">
			<div class="pull-left">
				<img class="img-rounded" src="${company.logo_url}" width="150"
					height="150" />
			</div>
			<div class="span3">
				<h3>
					<c:out value="${company.name}" />
				</h3>
				<span><c:out value="${company.follower_count}" /> Followers</span>
				<br> <span><c:out
						value="$${company.total_money_raised}M" /> Total Funding</span>
				<hr class="space" />
				<hr class="space" />
				<c:forEach items="${company.locations}" var="location">
					<span class="pull-left muted"> <small>${location.name}
					</small></span>
				</c:forEach>
				<br>
			</div>
			<div class="span3">
				<hr class="space" />
				<hr class="space" />
				<span class="pull-left">Website: </span> <a
					class="pull-left" href="${company.company_url}">${company.company_url}</a>
				<hr class="space" />
				<br>
				<hr class="space" />
				<a id="profileTwitterIcon" target="_blank"
					href="${company.twitter_url}"> <img class="img-rounded"
					src="resources/img/pro-twitter-icon.png" width="30" height="30"></a>
				<a id="profileAnglelistInIcon" target="_blank"
					href="${company.angellist_url}"> <img class="img-rounded"
					src="resources/img/pro-anglelist-icon.png" width="100"
					height="100"></a>
			</div>
			</div>
		</div>
	</div>
	<hr class="space" />

	<div class="span11 well">
		<div class="row">
			<div class="span11">
				<h4>About</h4>
				<span><c:out value="${company.overview}" /></span>
			</div>
		</div>
	</div>
	
	<div class="span11 well">
		<div class="row">
			<div class="span11">
				<h4>Funding</h4>
					<span> 
						<c:forEach items="${company.fund_info}" var="fund">
						<div class="pull-left span10">
							<span class="pull-left muted">Series  ${fund.round_code}</span> 
							<span class="pull-right muted">${fund.funded_month}/${fund.funded_day}/${fund.funded_year} </span>
							<br>
							<span class="pull-left large-number">$${fund.raised_amount_in_million}M</span>
							<br>
							<span class="pull-left"><small>${fund.allInvestorNames}</small></span>
							<br>
							<hr>
						</div>
					</c:forEach>
				</span>
			</div>
		</div>
	</div>

	<div class="span11 well">
		<div class="row">
			<div class="span11">
				<h4>Investors Invested</h4>
				<hr>
				<h5>Individual Investors</h5>
				<span> <c:forEach items="${personInvested}" var="investor">
					<div class="pull-left">
						<img class="img-rounded" src="${investor.image}" width="50" height="50" /> 
						<span class="pull-left "><a href=investorProfile?permalink=${investor.permalink}><c:out value="${investor.name}" /></a></span>
						<hr class="space" />
					</div>
					</c:forEach>
				</span>
				<hr class="space" />
				<hr>
				<hr class="space" />
			</div>

			<div class="span11">
				<h5>Institutional Investors</h5>
				<span> <c:forEach items="${finOrgInvested}" var="finOrg">
						<div class="pull-left">
							<img class="img-rounded" src="${finOrg.logo_url}" width="50"
								height="50" /> <span class="pull-left "> <a
								href=financialOrgProfile?permalink=${finOrg.permalink}><c:out
										value="${finOrg.name}" /></a>
							</span>
							<hr class="space" />
						</div>

					</c:forEach>
				</span>
				<hr class="space" />
				<hr>
				<hr class="space" />
			</div>

			<div class="span11">
				<h5>Company Investors</h5>
				<span> <c:forEach items="${companyInvested}" var="company">
						<div class="pull-left">
							<img class="img-rounded" src="${company.logo_url}" width="50"
								height="50" /> <span class="pull-left "> <a
								href=companyProfile?permalink=${company.permalink}><c:out
										value="${company.name}" /></a>
							</span>
							<hr class="space" />
						</div>

					</c:forEach>
				</span>
			</div>
		</div>
	</div>
</div>
</body>
</html>
