<%@page contentType="text/html" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
				<br><br>
				<form action="<c:url value='/adminController/totalinvoice.jsp' />" method ="get">
					<input type="submit" value="Get overall invoice report">
				</form >
				<br><br>
				<form action = "<c:url value='/adminController/totaldownload.jsp' />" method="get" >
					<input type="submit" value="Get overall download report">
				</form>
			</section>
			<jsp:include page='/includes/footer.jsp' />



