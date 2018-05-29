<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
				<h1>List of Albums</h1>
				<table>
				<tr>
				  <tr>
					<th>Track Title</th>
					<td></td>
					<th>Sample MP3</th> 
				  </tr>
				</tr>
				  <c:forEach var="tracks" items="${track}">
					<tr>
					  <tr>
						<td>${tracks.title}</td>
						<td></td>
						<td><a href ="<c:url value = '/catalog/download?trackno=${tracks.id}' />">${tracks.sampleFilename}</a></td>
					  </tr>
					</tr>
				  </c:forEach>
				  <tr></tr>
				</table>
				<br>
				<br>
				<br>
				<form action="<c:url value='/catalog/addtocart' />" method="get"> 
					<input type="hidden" name="id" value="product">
					<input type="submit" value=" Add the album to your cart" >
				</form>
			</section>
			<jsp:include page='/includes/footer.jsp' />