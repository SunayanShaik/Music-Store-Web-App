<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page='/includes/header.jsp' />
<jsp:include page='/includes/sidebar.jsp' />

<section class ="sidebarPlayarea">
	<td width="404" valign="top">
	  <p>To listen to a track, click on the audio format.</p>
	<table>
		<tr>
		  <td>6. 64 Corvair, Part 2</td>
		  <td><a href="<c:url value='/sound/pf01/corvair.mp3'/>" >MP3</a></td>
		</tr>
		<tr>
		  <td>2. Whiskey Before Breakfast</td>
		  <td><a href="<c:url value='/sound/pf01/whiskey.mp3'/>" >MP3</a></td>
		</tr>
	  </table>
	</td>
</section>
<jsp:include page='/includes/footer.jsp' />
	

