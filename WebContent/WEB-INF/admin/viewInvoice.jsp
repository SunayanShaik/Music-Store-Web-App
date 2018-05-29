<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='/includes/header.jsp' />
<jsp:include page='/includes/sidebar.jsp' />
<section class="sidebarPlayarea">
<h1>List of Unprocessed Invoices</h1>
	<table border="1">
		<th>InvoiceId</th>
		<th>InvoiceDate</th>
		<th>Total Amount</th>
		<tr>
			<td>${invoiceInfo.invoiceId}</td>
			<td>${invoiceInfo.invoiceDate}</td>
			<td>${invoiceInfo.totalAmount}</td>
		</tr>
	</table>
	<br/>
	<a href="<c:url value = '/adminController/processInvoice.html?id=${invoiceInfo.invoiceId}' />">Process Invoice</a>
</section>