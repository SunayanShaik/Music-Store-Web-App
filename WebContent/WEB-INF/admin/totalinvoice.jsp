<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
					<br><br>
					<table>
							<tr align="justify">
								<th>Invoice ID</th>
								<th>&nbsp; </th>
								<th>User Name</th> 
								<th>&nbsp; </th>
								<th>Date</th>
								<th> &nbsp; </th>
								<th>Amount</th>
								<th> &nbsp; </th>
								<th>IsProcessed</th>
						    </tr>
							<c:forEach var="invoice" items="${invoiceDataSet}">
								<tr align="justify">
									<td  align="justify">${invoice.invoiceId}</td>
									<th>&nbsp; </th>
									<td  align="justify">${invoice.userFullName}</td>
									<th>&nbsp; </th>
									<td  align="justify">${invoice.invoiceDate}</td>
									<th>&nbsp; </th>
									<td  align="justify">&#36 ${invoice.totalAmount}</td>
									<th>&nbsp; </th>
									<c:choose>
										<c:when test='${invoice.processed eq true}'>
											<td>Y</td>
										</c:when>
										<c:otherwise>
											<td>N</td>
										</c:otherwise>
									</c:choose>
								</tr>
						  </c:forEach>
					</table>
			</section>
			<jsp:include page='/includes/footer.jsp' />



