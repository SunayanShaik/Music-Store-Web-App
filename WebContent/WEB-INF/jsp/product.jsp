<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
			
			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />	
			<section class ="sidebarPlayarea">
				<!--<c:forEach  end= "1"  var="tracks" items="${track}">
					<p style="visibility: hidden;">${t=tracks}</p>
				</c:forEach>-->
				<h1>${t.product.description}</h1>
				<p>Product Code: ${t.product.code}</p>
				<p>Discounted price &#36<i>${t.product.price}</i></p>
				<br><br>
				<a href="<c:url value='/catalog/track.jsp' />">Explore the Album!</a>
				<br>
				<br>
				<br>
				<form action="<c:url value ='/catalog/addtocart' />" method="get">  
					<input type="hidden" name="id" value="product">
					<input type="submit" value=" Add the album to your cart" >
				</form>
			</section>
		