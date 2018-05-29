<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<section class="sidebarPlayarea">
<h1>Initialize Database</h1>

<p>Database has been initialized</p>
<br/>
<br/><br>
<a href="<c:url value = '/adminController/welcome.html' />">Back to Home Page</a>
</section>