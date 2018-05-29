<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
				<h1>Your Cart.</h1>
				<c:choose>
					<c:when test='${empty dummyCartItem.quantity}'>
						<h4> Your Cart is empty.We are here to make it full!<h4>
					</c:when>
					<c:otherwise>				
						<table class="cart">
						<tr align="justify">
							<th>Qty</th>
							<th>&nbsp; </th>
							<th>Product Description</th> 
							<th>&nbsp; </th>
							<th>Price</th>
							<th> &nbsp; </th>
							<th>Amount</th>
							<th style = "visibility: hidden;">${count = 0}</th>
						  </tr>
						  <c:forEach var="cartItem" items="${cartItemData}">
								<tr align="justify">
									<td  align="justify">
										<form action="<c:url value='/catalog/updateCart'  />" method="get" >
											<input type="text" value ="${cartItem.quantity}" name="quantity">
											<input type="hidden" name="id" value="${cartItem.productId}">
											<input type="submit" value = "Change" name ="update">						
										</form>
									</td>
									<th>&nbsp; </th>
									<td  align="justify">${cartItem.description}</td>
									<th>&nbsp; </th>
									<td  align="justify">&#36 ${cartItem.price}</td>
									<th>&nbsp; </th>
									<td  align="justify">&#36 ${amount[count]}</td>
									<th>&nbsp; </th>
									<td  align="justify">
										<a href="<c:url value='/catalog/deleteCart?id=${cartItem.productId}'  />">	<input type="submit" value ="Remove" name="delete"> </a>
									</td  align="justify">
									<td style = "visibility: hidden;">${count = count + 1}</td>
								</tr>
						  </c:forEach>
						</table>
						  <br>					
						  <br>
						  <p><b>Total:&#36 ${totalAmount}</b></p>
						<section>
						<br>
							<a href="<c:url value='/catalog/catalog' />">
								<input type = "submit" value="Continue Shooping!" name="shopping">
							</a>
							<span>      &nbsp;</span>
							<a href="<c:url value='/sales/generateInvoice'  />">
								<input type = "submit" value="Checkout" name="checkout">
							</a>	
						</section>
					</c:otherwise>			
				</c:choose>
			</section>
			<jsp:include page='/includes/footer.jsp' />