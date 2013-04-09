<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@include file="../../resources/include/head.jsp"%>

<script language="javascript">
	function selectToggle(toggle, form) {
		var myForm = document.forms[form];
		for ( var i = 0; i < myForm.length; i++) {
			if (toggle) {
				myForm.elements[i].checked = "checked";
			} else {
				myForm.elements[i].checked = "";
			}
		}
	}
	
	function load(){
		showFundFilter();
	}

	function showFundFilter(){
		var checkVal = new Array(0,${total_funding});
		for(var i=1; i<checkVal.length; i++){
			document.getElementById("tf"+checkVal[i]).checked="checked";
		}
	}
	
	function check(form,id){
		var flag=false;
		var myForm = document.forms[form];
		for ( var i = 0; i < myForm.length-1; i++) {
			if (myForm.elements[i].checked) {
				flag=true;
			} 
		}
		if(!flag){
			alert("Please select at least one!");
			for ( var i = 1; i < myForm.length; i++){
				document.getElementById(id+i).checked="checked";
			}
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
					<strong>Followers</strong> <small class="pull-right"><a>Select
							All</a> | <a href="#">None</a></small>
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

			<div class="side-block hidden-phone">
				<form name="tform" method="POST" action="fundFilter">
					<div>
						<strong>Total Fund Raised</strong> <small class="pull-right"><a
							href="javascript:selectToggle(true, 'tform');">Select All</a> | <a
							href="javascript:selectToggle(false, 'tform');">None</a></small>
					</div>
					<hr class="space" />
					<ul class="clear-ul">

						<li class="mts"><input type="checkbox" 
							name="total_funding" id="tf1" value="1">&nbsp;0 - 500k
						<li class="mts"><input type="checkbox" 
							name="total_funding" id="tf2" value="2">&nbsp;500k - 1M
						<li class="mts"><input type="checkbox" 
							name="total_funding" id="tf3" value="3">&nbsp;1M - 5M
						<li class="mts"><input type="checkbox"
							name="total_funding" id="tf4" value="4">&nbsp;5M - 100M
						<li class="mts"><input type="checkbox" 
							name="total_funding" id="tf5" value="5">&nbsp;100M +
					</ul>
					<hr class="space" />
					<input type="submit" value="Update" onClick="check('tform','tf')">
				</form>
			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<form name="tform2" method="POST" action="locationFilter">
					<div>
						<strong>Company Location</strong> <small class="pull-right"><a
							href="javascript:selectToggle(true, 'tform2');">Select All</a> | <a
							href="javascript:selectToggle(false, 'tform2');">None</a></small>
					</div>
					<hr class="space" />
					<ul class="clear-ul">

						<li class="mts"><input type="checkbox" checked="checked"
							name="location" value="1">&nbsp;San Francisco <a
							class="small-text pull-right" href="#">only</a></li>

						<li class="mts"><input type="checkbox" checked="checked"
							name="location" value="2">&nbsp;New York, NY <a
							class="small-text pull-right" href="#">only</a></li>

						<li class="mts"><input type="checkbox" checked="checked"
							name="location" value="3">&nbsp;San Jose <a
							class="small-text pull-right" href="#">only</a></li>

						<li class="mts"><input type="checkbox" checked="checked"
							name="location" value="4">&nbsp;All Other Places<a
							class="small-text pull-right" href="#">only</a></li>
					</ul>

					<hr class="space" />
					<input type="submit" value="Update">
				</form>
			</div>
		</div>



		<div class="span9">

			<h1>Companies</h1>
			<hr>
			<c:forEach items="${companies}" var="company">
				<div class="search-result">
					<div class="pull-left search-pic">
						<img src="${company.logo_url}" width="50" height="50" />
					</div>

					<div class="pull-left search-content">
						<div>
							<h4 class="pull-left">
								<a href=companyProfile?id=${company.id}><c:out value="${company.name}" /></a>
							</h4>
						</div>
						<div>
							<span class="pull-left ">${company.high_concept}</span>
						</div>
						<div>
							<c:forEach items="${company.locations}" var="location">
								<span class="pull-left muted"> <small>${location.name}
								</small></span>
								<hr class="space" />
							</c:forEach>
						</div>
					</div>


					<div class="search-stats pull-right">
						<div>
							<span class="large-number"><c:out
									value="${company.follower_count}" /></span> Followers
						</div>
						<div>
							<span class="large-number"><c:out
									value="$${company.total_funding}M" /></span> Total Funding
						</div>
					</div>

					<div class="clearfix"></div>
					<hr class="space" />

				</div>
				<hr class="space" />
			</c:forEach>
		</div>


	</div>






</div>


</body>
</html>
