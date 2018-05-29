<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
				<h1>List of Albums</h1>
				<table>
				  <tr>
					<th>Product Code</th>
					<th>Product Description</th> 
					<th>Product Price</th>
				  </tr>
				  <c:forEach var="product" items="${productSet}">
					  <tr>
						<td>${product.code}</td>
						<td><a href ="<c:url value = '/catalog/product.jsp?id=${product.id}' />">${product.description}</a></td>
						<td>${product.price}</td>
					  </tr>
				  </c:forEach>
				</table>
			</section>
			<jsp:include page='/includes/footer.jsp' />