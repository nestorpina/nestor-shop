<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<!DOCTYPE html>
<html lang="en" ng-app="nestorshop">
<head>
<title>Nestor-shop Demo App</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="js/jquery1.9.0.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/angular/angular.min.js"></script>
<script src="js/angular/angular-resource.min.js"></script>
<script src="js/controllers.js"></script>
<script src="js/app.js"></script>
<script src="js/services.js"></script>
<script src="js/utils.js"></script>
<script src="js/jquery-ui-1.10.2.custom.min.js"></script>

<script>
$(function() {
	$('.nav li a').on('click', function() {
	    $(this).parent().parent().find('.active').removeClass('active');
	    $(this).parent().addClass('active');
	});
	// Close button on alerts
	$(".alert").alert()
});
</script>
</head>
<body style="padding-top:40px">
<%
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
if (user != null) {
	pageContext.setAttribute("user", user);	
}
%>
<div class="container">

<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<span class="brand">Nestor-Shop</span>
	    <ul class="nav">
	    <li class="active">
	    	<a href="#products">Products</a>
	    </li>
	    <li><a href="#shoplists">My Lists</a></li>
	    <li class="divider-vertical"></li>
	    <li><a href="#shoplists" id="currentSL">Current List : none</a></li>
	    <li class="divider-vertical"></li>
	    <li><a href="#">Logged as: ${user.nickname}
		    <% if(userService.isUserAdmin()) { %>
				<small>(administrator)</small>
			<% } %>
		</a></li>
	    <li><a href="<%= userService.createLogoutURL("/") %>" data-ajax="false">Logout</a></li>
	    </ul>
	</div>
</div>
<div id="messages"></div>
<div class="alert alert-block alert-error fade in hide" id="listNotSelectedAlert">
	<button type="button" class="close" data-dismiss="alert">×</button>
   <p>You must select a shopping list before adding products.</p>
</div>

<div ng-view></div>

</div>
</body>
</html>