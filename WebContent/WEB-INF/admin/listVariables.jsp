<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<section class ="sidebarPlayarea">

<h1>Cookies in this request: </h1>
<!-- cookie is an implicit object of JSP, see pg. 217 -->
<c:forEach items="${cookie}" var="cook">
<!-- cook is a map entry, with key (cookie name) and value (cookie object) -->
	  <br>Cookie:${cook.key} = ${cook.value.value}
</c:forEach>


<h1>Session vars in this session:</h1>
<!-- sessionScope is an implicit object of JSP, a map of session vars -->
<c:forEach items="${sessionScope}" var="v">
<!-- v is a map entry, with key (var name) and value (var object) -->
	  <br>Session var: ${v.key} = ${v.value} 
</c:forEach>

</section>