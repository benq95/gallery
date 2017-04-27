<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Gallery</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="webjars/bootstrap/3.3.7-1/css/bootstrap.css">
    <!-- Custom CSS -->
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>


	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<ul class="nav navbar-nav navbar-center">
				<p class="navbar-text">Gallery</p>
			</ul>
		</div>
	</nav>
	<div class="container">

		<div class="row">
			<div class="box">
				<div class="col-lg-12 text-center">
					<h1 class="brand-name">SIGN IN</h1>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="box">
				<div class="col-xs-6 col-sm-4"></div>
				<div class="col-xs-6 col-sm-4">
					<form action="<c:url value="/login" />" method="post">
						<div class="form-group <c:if test="${error!=null}">has-error</c:if>">
							<label for="username" class="login-text">Username:</label> <input type="text"
								class="form-control <c:if test="${error!=null}">form-control-danger</c:if>" id="email" placeholder="Username" name="username">
						</div>
						<div class="form-group <c:if test="${error!=null}">has-error</c:if>">
							<label for="pwd" class="login-text" >Password:</label> <input type="password"
								class="form-control <c:if test="${error!=null}">form-control-danger</c:if>" id="pwd" placeholder="Password" name="password">
						</div>
						<span class="help-block">${error}</span>
						<button type="submit" value=" Send" class="btn btn-primary btn-lg btn-block">LOGIN</button>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					</form>
				</div>
				<div class="col-xs-6 col-sm-4"></div>
			</div>
		</div>
	</div>



	<script type="text/javascript" src="webjars/jquery/3.2.0/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
</body>
</html>
