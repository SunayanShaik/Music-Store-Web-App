<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='/includes/header.jsp' />
<jsp:include page='/includes/sidebar.jsp' />

<section class ="sidebarPlayarea">
	  <p>To listen to a track, click on the audio format.</p>
	<table>
		<tr>
		  <td width="200"><b>Song title</b></td>
		  <td width="100"><b>Audio Format</b></td>
		</tr>
		<tr>
		  <td>2. You Are a Star</td>
		  <td><a href="<c:url value='/sound/8601/star.mp3'/>" >MP3</a></td>
		</tr>
		<tr>
		  <td>3. Don't Make No Difference</td>
		  <td><a href="<c:url value='/sound/8601/no_difference.mp3'/>" >MP3</a></td>
		</tr>
	  </table>
</section>
<jsp:include page='/includes/footer.jsp' />
