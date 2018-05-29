<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
			<meta charset="utf-8">
			<title>Raga</title>
			<link rel="stylesheet" href = "<c:url value='/styles/header.css' />">
			<link rel="stylesheet" href = "<c:url value='/styles/sidebar.css' />">
			<c:url var="welcomeURL"  value="/catalog/welcome.jsp" />
	</head>
	<body>
		<ul class="headerul">
			<li class="headerli"><a class="headera" href="<c:url value='/catalog/cartpage' />"><img src="<c:url value='/images/cart.png' />"></a></li>
			<li class="headerli" id="homeButton"><a class="headera" href="${welcomeURL}">Home</a></li>
			<li class="headerli" id="companyName">Raga..</li>
		</ul>
		<div id= "voilinImage"></div>
