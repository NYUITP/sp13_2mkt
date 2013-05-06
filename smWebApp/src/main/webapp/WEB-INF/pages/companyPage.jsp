<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@include file="../../resources/include/head.jsp"%>

<script type="text/javascript">


	function showValueCompanyFollower(newValue) {
		if (newValue == 1) {
			document.getElementById("company-follower-value").innerHTML = "Not Important";
			document.getElementById("company-follower-value").style.fontSize = "small"
			document.getElementById('comfollowersImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("company-follower-value").innerHTML = "A Little Important";
			document.getElementById("company-follower-value").style.fontSize = "small"
			document.getElementById('comfollowersImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("company-follower-value").innerHTML = "Moderately Important";
			document.getElementById("company-follower-value").style.fontSize = "small"
			document.getElementById('comfollowersImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("company-follower-value").innerHTML = "Important";
			document.getElementById("company-follower-value").style.fontSize = "small"
			document.getElementById('comfollowersImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("company-follower-value").innerHTML = "Very Important";
			document.getElementById("company-follower-value").style.fontSize = "small"
			document.getElementById('comfollowersImpLevel').value = 5;
		}
	}
	$(document).ready(load);

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
		showLocationFilter();
		showTimeRange(document.getElementById("timeRange").value);
		showValueCompanyFollower(document.getElementById("companyFollowerRange").value);
	}
	
	function showFundFilter() {
		var checkedVal = [${total_funding}];
		if(checkedVal == ""){
			selectToggle(true, 'tform');
		}
		for (i in checkedVal) {
			checkbox_id = "tf" + checkedVal[i];
			try {
				document.getElementById(checkbox_id).checked="checked";	
			} catch (err) {
				alert(checkbox_id);
			}
		}
	}
	
	function showLocationFilter() {
		var checkedVal = [${location}];
		if(checkedVal == ""){
			selectToggle(true, 'tform2');
		}
		for (i in checkedVal) {
			checkbox_id = "lct" + checkedVal[i];
			try {
				document.getElementById(checkbox_id).checked="checked";	
			} catch (err) {
				alert(checkbox_id);
			}
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
	
	function only(index,form){
		var myForm = document.forms[form];
		for ( var i = 0; i < myForm.length-1; i++){
			if(i==index-1)
				myForm.elements[i].checked="checked";
			else
				myForm.elements[i].checked="";
		}
	}
	
	function RemoveContent(d) {
		document.getElementById(d).style.visibility = "hidden";
	}

	function InsertContent(d) {
		document.getElementById(d).style.visibility = "visible";
	}
	function hide(){
		document.getElementByTagName("a").style.visibility = "hidden";
	}
	
	function showTimeRange(newValue) {
		if (newValue == 1) {
			document.getElementById("fund-period").innerHTML = "3 month";
			document.getElementById("fund-period").style.fontSize = "small"
			document.getElementById('periodPast').value = 1;
		} else if (newValue == 2) {
			document.getElementById("fund-period").innerHTML = "6 month";
			document.getElementById("fund-period").style.fontSize = "small"
			document.getElementById('periodPast').value = 2;
		} else if (newValue == 3) {
			document.getElementById("fund-period").innerHTML = "1 year";
			document.getElementById("fund-period").style.fontSize = "small"
			document.getElementById('periodPast').value = 3;
		} else if (newValue == 4) {
			document.getElementById("fund-period").innerHTML = "2 year";
			document.getElementById("fund-period").style.fontSize = "small"
			document.getElementById('periodPast').value = 4;
		} else if (newValue == 5) {
			document.getElementById("fund-period").innerHTML = "3 year";
			document.getElementById("fund-period").style.fontSize = "small"
			document.getElementById('periodPast').value = 5;
		}
	}

	 var pager = new Pager('companies', 3); 
     pager.init(); 
     pager.showPageNav('pager', 'pageNavPosition'); 
     pager.showPage(1);
</script>

<div class="container">
	<hr class="space" />

	<%@include file="../../resources/include/header.jsp"%>
	<hr class="space" />
	<div class="row">
		<div class="span3">
			<div class="side-block">
				<div>
					<strong>Company Type</strong>
				</div>
				<ul class="clear-ul">
					<li class="mts"> <small><input type="checkbox" checked="checked">&nbsp;Private Companies</small></li>
					<li class="mts"> <small><input type="checkbox" checked="checked">&nbsp;Public Companies </small></li>
				</ul>
				<hr class="space" />
				<a href="#" class="btn">Update</a>
			</div> 
			
			<hr class="space" />
			<div class="side-block hidden-phone">
				<div>
					<strong>Rank Company by Fund raised in last</strong>
				</div>
				<hr class="space" />

				<input id="timeRange" type="range" min="1" max="5" step="1"
					value="${periods}" onchange="showTimeRange(this.value)" /> <span
					id="fund-period" class="slider-value">1 year</span>

				<hr class="space" />
				<form action="companyRankingByFundTime" method="post">
					<input type='hidden' id='periodPast' name='periodPast' value='3' />
					<input type="submit" value="Update">
				</form>
			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of Followers</strong>
				</div>
				<hr class="space" />

				<input id="companyFollowerRange" type="range" min="1" max="5"
					step="1" value="${companyfollowerLevel}"
					onchange="showValueCompanyFollower(this.value)" /> <span
					id="company-follower-value" class="slider-value">Moderately
					Important</span>

				<hr class="space" />
				<form action="companyRankingByFollowers" method="post">
					<input type='hidden' id='comfollowersImpLevel'
						name='comfollowersImpLevel' value='3' /> <input type="submit"
						value="Update">
				</form>
			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<form name="tform" method="POST" action="companyFundFilter">
					<div>
						<strong>Total Fund Raised</strong> <small class="pull-right"><a
							href="javascript:selectToggle(true, 'tform');">Select All</a> | <a
							href="javascript:selectToggle(false, 'tform');">None</a></small>
					</div>
					<hr class="space" />
					<ul class="clear-ul">

						<li class="mts" onMouseOver="InsertContent('only1')"
							onMouseOut="RemoveContent('only1')"><input type="checkbox"
							name="total_funding" id="tf1" value="1">&nbsp;0 - 50k<a
							id="only1" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(1,'tform');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('only2')"
							onMouseOut="RemoveContent('only2')"><input type="checkbox"
							name="total_funding" id="tf2" value="2">&nbsp;50k - 1M<a
							id="only2" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(2,'tform');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('only3')"
							onMouseOut="RemoveContent('only3')"><input type="checkbox"
							name="total_funding" id="tf3" value="3">&nbsp;1M - 3M<a
							id="only3" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(3,'tform');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('only4')"
							onMouseOut="RemoveContent('only4')"><input type="checkbox"
							name="total_funding" id="tf4" value="4">&nbsp;3M - 5M<a
							id="only4" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(4,'tform');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('only5')"
							onMouseOut="RemoveContent('only5')"><input type="checkbox"
							name="total_funding" id="tf5" value="5">&nbsp;5M +<a
							id="only5" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(5,'tform');">only</a></li>
					</ul>
					<hr class="space" />
					<input type="submit" value="Update" onClick="check('tform','tf')">
				</form>
			</div>

			<hr class="space" />

			<div class="side-block hidden-phone">
				<form name="tform2" method="POST" action="companyLocationFilter">
					<div>
						<strong>Company Location</strong> <small class="pull-right"><a
							href="javascript:selectToggle(true, 'tform2');">Select All</a> |
							<a href="javascript:selectToggle(false, 'tform2');">None</a></small>
					</div>
					<hr class="space" />
					<ul class="clear-ul">

						<li class="mts" onMouseOver="InsertContent('l1')"
							onMouseOut="RemoveContent('l1')"><input type="checkbox"
							name="location" id="lct1" value="1">&nbsp;<small>San Francisco</small><a
							id="l1" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(1,'tform2');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('l2')"
							onMouseOut="RemoveContent('l2')"><input type="checkbox"
							name="location" id="lct2" value="2">&nbsp;<small>New York, NY</small><a
							id="l2" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(2,'tform2');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('l3')"
							onMouseOut="RemoveContent('l3')"><input type="checkbox"
							name="location" id="lct3" value="3">&nbsp;<small>Los Angeles</small><a
							id="l3" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(3,'tform2');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('l4')"
							onMouseOut="RemoveContent('l4')"><input type="checkbox"
							name="location" id="lct4" value="4">&nbsp;<small>Toronto</small><a
							id="l4" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(4,'tform2');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('l5')"
							onMouseOut="RemoveContent('l5')"><input type="checkbox"
							name="location" id="lct5" value="5">&nbsp;<small>London</small><a
							id="l5" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(5,'tform2');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('l6')"
							onMouseOut="RemoveContent('l6')"><input type="checkbox"
							name="location" id="lct6" value="6">&nbsp;<small>Tokyo</small><a
							id="l6" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(6,'tform2');">only</a></li>
						<li class="mts" onMouseOver="InsertContent('l7')"
							onMouseOut="RemoveContent('l7')"><input type="checkbox"
							name="location" id="lct7" value="7">&nbsp;<small>All Other Places</small><a
							id="l7" style="visibility: hidden;"
							class="small-text pull-right" href="javascript:only(7,'tform2');">only</a></li>
					</ul>
	
					<hr class="space" />
					<input type="submit" value="Update" onClick="check('tform2','lct')">
				</form>
			</div>
		</div>
		<hr class="space" />

		<div class="span9">
			<h2>Companies</h2>
			<hr>
			<c:forEach items="${companies}" var="company">
				<div class="search-result">
					<div class="pull-left search-pic">
						<img src="${company.logo_url}" width="50" height="50" />
					</div>

					<div class="pull-left search-content">
						<div>
							<h4 class="pull-left">
								<a href=companyProfile?permalink=${company.permalink}>
								<c:out value="${company.name}" /></a>
							</h4>
						</div>
						
						<div class="pull-left">
							<c:forEach items="${company.locations}" var="location">
								<span class="muted"> <small>${location.name}
								</small></span>
								<br>
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
									value="$${company.total_money_raised}M" /></span> Total Funding
						</div>
					</div>

					<div class="clearfix"></div>
					<hr class="space" />

				</div>
				<hr class="space" />
				<hr>
			</c:forEach>
		</div>
		<div class="pagination pagination-large pagination-centered">
			<ul>
				<c:if test="${currentPage != 1}">
					<li><a href=companies?page=${currentPage-1}>&laquo;</a></li>
				</c:if>
				<c:if test="${currentPage == 1}">
					<li class="disabled"><a href=#>&laquo;</a></li>
				</c:if>

				<c:forEach begin="1" end="${noOfPages}" var="i">
					<c:choose>
						<c:when test="${currentPage eq i}">
							<li class="active"><a href=companies?page=${i}>${i}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href=companies?page=${i}>${i}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:if test="${currentPage lt noOfPages}">
					<li><a href=companies?page=${currentPage+1}>&raquo;</a></li>
				</c:if>
				<c:if test="${currentPage == noOfPages}">
					<li class="disabled"><a href=#>&raquo;</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</div>
</body>
</html>
