<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page='/includes/header.jsp' />
<jsp:include page='/includes/sidebar.jsp' />
<section class="sidebarPlayarea">
	<h1>List of Unprocessed Invoices</h1>
	<table border="1">
		<th>InvoiceId</th>
		<th>Actions</th>
		<tr>
	<c:forEach var="unprocInv" items="${unprocInvList}">
			<tr>
				<td>${unprocInv.invoiceId}</td>
				<td><a href="<c:url value = '/adminController/viewInvoice.html?id=${unprocInv.invoiceId}' />">View Invoice</a></td>
			</tr>
	</c:forEach>
	</tr>
	</table>
	<c:if test="${fn:length(unprocInvList) eq 0}">
		<p>No invoices to process.</p>
	</c:if>
</section>
