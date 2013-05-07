<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@include file="../../resources/include/head.jsp"%>


<script type="text/javascript">

	$(document).ready(load);
	
	$(function() {  
	    $('span.stars').stars();
	});
	
	$.fn.stars = function() {
	    return $(this).each(function() {
	       $(this).html($('<span />').width(parseFloat($(this).html()) * 80));
	    });
	}
	
		
	function RemoveContent(d) {
		document.getElementById(d).style.visibility = "hidden";
	}

	function InsertContent(d) {
		document.getElementById(d).style.visibility = "visible";
	}
	
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

	function load() {
		showLocationFilter();
		showValueInvestor(document.getElementById("followerRange").value);
		showValueCompany(document.getElementById("companyRange").value);
		showValueROI(document.getElementById("roiRange").value);
//		showStarInvestor(document.getElementById("starFilter").value);

	}
	function showValueInvestor(newValue) {
		if (newValue == 1) {
			document.getElementById("follower-value").innerHTML = "Not Important";
			document.getElementById("follower-value").style.fontSize = "small"
			document.getElementById('followersImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("follower-value").innerHTML = "A Little Important";
			document.getElementById("follower-value").style.fontSize = "small"
			document.getElementById('followersImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("follower-value").innerHTML = "Moderately Important";
			document.getElementById("follower-value").style.fontSize = "small"
			document.getElementById('followersImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("follower-value").innerHTML = "Important";
			document.getElementById("follower-value").style.fontSize = "small"
			document.getElementById('followersImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("follower-value").innerHTML = "Very Important";
			document.getElementById("follower-value").style.fontSize = "small"
			document.getElementById('followersImpLevel').value = 5;
		}
	}

	function showValueCompany(newValue) {
		if (newValue == 1) {
			document.getElementById("company-value").innerHTML = "Not Important";
			document.getElementById("company-value").style.fontSize = "small"
			document.getElementById('companyImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("company-value").innerHTML = "A Little Important";
			document.getElementById("company-value").style.fontSize = "small"
			document.getElementById('companyImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("company-value").innerHTML = "Moderately Important";
			document.getElementById("company-value").style.fontSize = "small"
			document.getElementById('companyImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("company-value").innerHTML = "Important";
			document.getElementById("company-value").style.fontSize = "small"
			document.getElementById('companyImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("company-value").innerHTML = "Very Important";
			document.getElementById("company-value").style.fontSize = "small"
			document.getElementById('companyImpLevel').value = 5;
		}
	}
	
	function showValueROI(newValue) {
		if (newValue == 1) {
			document.getElementById("roi-value").innerHTML = "Not Important";
			document.getElementById("roi-value").style.fontSize = "small"
			document.getElementById('roiImpLevel').value = 1;
		} else if (newValue == 2) {
			document.getElementById("roi-value").innerHTML = "A Little Important";
			document.getElementById("roi-value").style.fontSize = "small"
			document.getElementById('roiImpLevel').value = 2;
		} else if (newValue == 3) {
			document.getElementById("roi-value").innerHTML = "Moderately Important";
			document.getElementById("roi-value").style.fontSize = "small"
			document.getElementById('roiImpLevel').value = 3;
		} else if (newValue == 4) {
			document.getElementById("roi-value").innerHTML = "Important";
			document.getElementById("roi-value").style.fontSize = "small"
			document.getElementById('roiImpLevel').value = 4;
		} else if (newValue == 5) {
			document.getElementById("roi-value").innerHTML = "Very Important";
			document.getElementById("roi-value").style.fontSize = "small"
			document.getElementById('roiImpLevel').value = 5;
		}
	}
	
	function showStarInvestor(newValue){
		if (newValue == 1){
			document.getElementById("star-value").innerHTML = "0.2";
			document.getElementById("starLevel").value = 1;
			$('span.starf').stars();
		} else if (newValue == 2){
			document.getElementById("star-value").innerHTML = "0.4";
			document.getElementById("starLevel").value = 2;
			$('span.starf').stars();
		} else if (newValue == 3){
			document.getElementById("star-value").innerHTML = "0.6";
			document.getElementById("starLevel").value = 3;
			$('span.starf').stars();
		} else if (newValue == 4){
			document.getElementById("star-value").innerHTML = "0.8";
			document.getElementById("starLevel").value = 4;
			$('span.starf').stars();
		} else if (newValue == 5){
			document.getElementById("star-value").innerHTML = "1.0";
			document.getElementById("starLevel").value = 5;
			$('span.starf').stars();
		}
	}

</script>
<div class="container">
	<hr class="space" />

	<%@include file="../../resources/include/header.jsp"%>

	<hr class="space" />
	<div class="row">

		<div class="span3">
 <!--  
			<div class="side-block hidden-phone">
				<div>
					<strong>Star Investors</strong>
				</div>
				<hr class="space" />
			
				<input id="starFilter" type="range" min="1" max="5" step="1" value="${starl}"
					onchange="showStarInvestor(this.value)" />
					
				<p><span id="star-value" class="starf">0.6</span></p>
				
				<hr class="space" />
				<form action="starsFilter" method="post">
					<input type='hidden' id='starLevel' name='starLevel' value='3'/>
					<input type="submit" value="Update">
				</form>
				
			</div>
			
			<hr class="space" />
-->		
			<div class="side-block hidden-phone">
				<div>
					<strong>Importance of Followers</strong>
				</div>
				<hr class="space" />

				<input id="followerRange" type="range" min="1" max="5" step="1" value="${followerLevel}"
					onchange="showValueInvestor(this.value)" />

				<span id="follower-value" class="slider-value">Moderately Important</span>
				<hr color=#DEDEDE size="3">

			<hr class="space" />

				<div>
					<strong>Importance of Companies Invested In</strong>
				</div>
				<hr class="space" />

				<input id="companyRange" type="range" min="1" max="5" step="1" value="${companyLevel}"
					onchange="showValueCompany(this.value)" />

				<span id="company-value" class="slider-value">Moderately Important</span>
			<hr color=#DEDEDE size="3">
			<hr class="space" />

				<div>
					<strong>Importance of Star Rating</strong>
				</div>
				<hr class="space" />

				<input id="roiRange" type="range" min="1" max="5" step="1" value="${roiLevel}"
					onchange="showValueROI(this.value)" />

				<span id="roi-value" class="slider-value">Moderately Important</span>

			<hr class="space" />
				<form action="investorRankingByFC_CC_ROI?page=1" method="post">
					<input type='hidden' id='companyImpLevel' name='companyImpLevel' value='3'/>
					<input type='hidden' id='followersImpLevel' name='followersImpLevel' value='3'/>
					<input type='hidden' id='roiImpLevel' name='roiImpLevel' value='3'/>
					<input type="submit" value="Update">
				</form>
</div>		
			<!-- <div class="side-block">
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

			</div> -->

			<hr class="space" />
			
			<div class="side-block hidden-phone">
				<form name="tform2" method="POST" action="investorLocationFilter?page=1">
					<div>
						<strong>Investor Location</strong> <small class="pull-right"><a
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

		<div class="span9">

			<h2>Investors</h2>
			<hr>
			<c:forEach items="${investors}" var="investor">
				<div class="search-result">
					<div class="pull-left search-pic">
						<img alt="${investor.name}" src="${investor.image}" width="50" height="50" />
					</div>

					<div class="pull-left search-content">
						<div>
							<h4 class="pull-left">
								<a href=investorProfile?permalink=${investor.permalink}><c:out value="${investor.name}" /></a>
							</h4>
							
						</div>
						<div class="pull-left">
							<c:forEach items="${investor.locations}" var="location">
							<span class="muted" > <small>${location.name} </small></span>
							<br>
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
						<div>
							<p><span class="stars">${investor.star_score}</span></p>
						</div>

					</div>
					<div class="clearfix"></div>

					<!-- <c:out value="${investor.bio}" /> -->
					<hr class="space" />
				</div>
				<hr>
			</c:forEach>
			<ul class="pager">
			<c:if test="${currentPage == 1}">
				<li class="previous disabled"><a
					href=#>&larr; Older</a></li>
			</c:if>
			<c:if test="${currentPage != 1}">
				<li class="previous"><a href=paginateInvestors?page=${currentPage-1}>&larr;
						Older</a></li>
			</c:if>
			<li>Showing results <c:out value="${startIndex}"></c:out>-<c:out value="${endIndex}"></c:out> of <c:out value="${size}"></c:out></li>
			<c:if test="${currentPage == noOfPages}">
				<li class="next disabled"><a
					href=#>&rarr; Newer</a></li>
			</c:if>
			<c:if test="${currentPage lt noOfPages}">
				<li class="next"><a href=paginateInvestors?page=${currentPage+1}>&rarr;	Newer</a></li>
			</c:if>
		</ul>
		</div>
		
	</div>
</div>
</body>
</html>
