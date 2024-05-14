<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="container">
	<form name="mForm" id="mForm">
		<div class="form-group">
			<label for="email">이메일</label> 
			<input type="text" name="email" class="form-control" placeholder="이메일을 입력해주세요" id="email">
		</div>
				
		<div class="form-group">
			<label for="pwd">비밀번호</label> 
			<input type="password" name="pwd" class="form-control" placeholder="비밀번호를 입력해주세요" id="pwd">
		</div>
	</form>
	<button id="btn-save" class="btn btn-primary">회원가입</button>

</div>

<script src="<c:url value='/js/member.js'/>"></script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>

