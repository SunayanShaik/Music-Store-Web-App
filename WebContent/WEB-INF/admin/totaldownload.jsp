<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
					<br><br>
					<table>
							<tr align="justify">
								<th>Product Code</th>
								<th>&nbsp; </th>
								<th>Email Address</th> 
								<th>&nbsp; </th>
								<th>Date</th>
								<th> &nbsp; </th>
								<th>TitleTrack</th>
						    </tr>
							<c:forEach var="download" items="${downloadDataSet}">
								<tr align="justify">
									<td  align="justify">${download.emailAddress}</td>
									<th>&nbsp; </th>
									<td  align="justify">${download.productCode}</td>
									<th>&nbsp; </th>
									<td  align="justify">${download.downloadDate}</td>
									<th>&nbsp; </th>
									<td  align="justify">${download.trackTitle}</td>
								</tr>
						  </c:forEach>
					</table>
			</section>
			<jsp:include page='/includes/footer.jsp' />