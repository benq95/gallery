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

    <link rel="stylesheet" href="webjars/bootstrap/3.3.7-1/css/bootstrap.css">    <!-- Custom CSS -->
    <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>


	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<ul class="nav navbar-nav navbar-center">
				<p class="navbar-text">Gallery</p>
			</ul>	
			<ul class="nav navbar-nav navbar-right">
				<li><a href="<c:url value="/logout"/>"><span class="glyphicon glyphicon-log-in"></span>
						Sign Out</a></li>
			</ul>
		</div>
	</nav>

	<div class="container">

        <div class="row">
            <div class="box">
                <div class="col-lg-12 text-center">
                    <h1 class="brand-name">EXPLORE YOUR PHOTOS</h1>
                </div>
            </div>
        </div>
		<c:forEach items="${photoList}" var="item">
        <div class="row">
            <div class="box" id="photo">
                <div class="col-lg-12 gallery">
					<a href="<c:url value="/images/${item }.jpeg"/>"
						class="col-lg-12"> 
						<img src="<c:url value="/images/${item }.jpeg"/>" class="img-fluid">
					</a>
				</div>
            </div>
        </div>
        </c:forEach>
      </div>



<script type="text/javascript" src="webjars/jquery/3.2.0/jquery.min.js"></script>
<script type="text/javascript" src="webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
</body>
</html>
