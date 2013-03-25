<%@include file="../../resources/include/head.jsp" %>

<div class="container">
	<hr class="space"/>
    
    <%@include file="../../resources/include/header.jsp" %>
	<hr class="space"/>

	<div class="row">

		<div class="span3">
			<div class="side-block hidden-phone">
				<div>
		            <strong>Followers</strong>
		            <small class="pull-right"><a >Select All</a> | <a href="#">None</a></small>
	        	</div>
	        	<hr class="space"/>
	        	<ul class="clear-ul">
	        		
	        		<li class="mts">
	        			<input type="checkbox" checked="checked">&nbsp;0-50</input>
	        			<a class="small-text pull-right" href="#">only</a>
	        		</li>
	        		
	        		<li class="mts">
	        			<input type="checkbox" checked="checked">&nbsp;51-100</input>
	        			<a class="small-text pull-right" href="#">only</a>
	        		</li>
	        		
	        		<li class="mts">
	        			<input type="checkbox" checked="checked">&nbsp;101-150</input>
	        			<a class="small-text pull-right" href="#">only</a>
	        		</li>
	        		
	        		<li class="mts">
	        			<input type="checkbox" checked="checked">&nbsp;150+</input>
	        			<a class="small-text pull-right" href="#">only</a>
	        		</li>
	        	</ul>
	        	
	        	<hr class="space"/>
	        	<a href="#" class="btn">Update</a>

			</div>

			<hr class="space"/>

		</div>


		<div class="span9">

			<h1>Companies</h1>
			<hr>

			<div class="search-result">
				<div class="pull-left search-pic">
					<img src="resources/img/company-logo.png" width="50" height="50"/>
				</div>
				
				<div class="pull-left search-content">
					<div><h4 class="pull-left">Company Name</h4></div>
					<div><a class="pull-left" href="#">Website</a></div>
				</div>

				<div class="search-stats pull-right">
					<div><span class="large-number">#</span> Followers</div>
					<div>Some other Number</div>
				</div>

				<div class="clearfix"></div>
				<hr class="space"/>
				Description
				

			</div>

		</div>


	</div>
	





</div>


</body>
</html>
