<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
				<h1>Your Invoice</h1>
				<section>
					<p>Name: ${userInvoiceData.userFullName}<p>
					<p>Invoice Date: ${userInvoiceData.invoiceDate}</p>
				</section>
				<section>
					<table>
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
									<td  align="justify">${cartItem.quantity}</td>
									<th>&nbsp; </th>
									<td  align="justify">${cartItem.description}</td>
									<th>&nbsp; </th>
									<td  align="justify">&#36 ${cartItem.price}</td>
									<th>&nbsp; </th>
									<td  align="justify">&#36 ${amount[count]}</td>
									<th>&nbsp; </th>
									<td style = "visibility: hidden;">${count = count + 1}</td>
								</tr>
						  </c:forEach>
					</table>
					<p><b>Total:&#36 ${totalAmount}</b></p>
				</section>
			</section>
			<jsp:include page='/includes/footer.jsp' />