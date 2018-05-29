<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<section class="sidebarPlayarea">
	<h1>Admin Menu Page</h1>
	<div>
		<ul>
			<li><form action="<c:url value ='/adminController/processInvoices.html' />" method="get">
				<input type="submit" value="Process Invoices"><br />
			</form></li>
			<li><form action="<c:url value = '/adminController/displayReports.html' />" method="get">
				<input type="submit" value="Display Reports"><br />
			</form></li>
			<li><form action= "<c:url value = '/adminController/initDB.html' />"  method="get">
				<input type="submit" value="Initialize Database"><br />
			</form></li>
			<li><form action= "<c:url value = '/adminController/logout.html' />" method="get">
				<input type="submit" value="Quit"><br />
			</form></li>
		</ul>

	</div>
</section>
<jsp:include page="/includes/footer.jsp" />