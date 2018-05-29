<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

			<jsp:include page='/includes/header.jsp' />
			<jsp:include page='/includes/sidebar.jsp' />
			<section class ="sidebarPlayarea">
				<h1>You Need to Register to try the samples</h1>
				<p>All the fields are required</p>
				<p><i>${message}</i></p>
				<form action="<c:url value='/sales/checkoutLogin' />" action="get">
					<label>FirstName: </label>
					<input type = "text" name="firstName" autofocus pattern="[a-zA-Z]+" required><br>
					<label>LastName: </label>
					<input type = "text" name="lastName" pattern="[a-zA-Z]+" required><br>
					<label>Email: </label>
					<input type = "email" name="email" pattern="(^[a-zA-Z0-9_.]+)@([a-zA-Z]+)\.([a-zA-Z]+){2,3}" required><br><br>
					<input type="submit" value="Register">
					<input type="reset" value="Reset">
				</form>
			</section>
			<jsp:include page='/includes/footer.jsp' />