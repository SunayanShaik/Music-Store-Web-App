<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<section class ="sidebarPlayarea">

<br/>
	<form action="<c:url value ='/adminController/adminWelcome.html' />" method="post">
		<label>Username:</label> 
		<input type="text" name="username"  /> <br/>
		<label>Password:</label>
		<input type="password" name="password"  /> <br/> 
		<input type="submit" value="Login" />
	</form>
</section>
<jsp:include page="/includes/footer.jsp" />