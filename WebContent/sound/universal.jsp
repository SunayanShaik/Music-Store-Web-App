<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html">
<html>
<head>
<title>Play sample</title>
</head>
<body>
	<p>Playing ${title} of CD ${user.product.productDescription} (universal.jap)</p>
	<audio controls autoplay>
		<source src="${src}" />
	</audio>
	<c:url var="userWelcomeUrl" value="userWelcome.html" />
	<c:url var="cartURL" value="cart.html" />
	<c:url var="addToCartUrl" value="cart.html">
		<c:param name="addItem" value="true" />
	</c:url>

	
	<UL><li><a href="${addToCartUrl}">Add to Cart</a></li>
		<li><a href="${userWelcomeUrl}">User Home </a></li>
		<li><a href="${cartURL}">View Cart</a></li>
	</UL>
</body>
</html>