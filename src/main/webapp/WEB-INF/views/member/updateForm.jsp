<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="container">
	<form>
		<input type="hidden" id="id" value="${principal.user.id}" />

		<div class="form-group">
			<label for="email">이메일</label> 
			<input type="text" name="email" value="${principal.user.email }"  class="form-control" placeholder="이메일을 입력해주세요" id="email" readonly>
		</div>
				
		<c:if test="${empty principal.user.oauth}">
			<div class="form-group">
				<label for="pwd">비밀번호</label> 
				<input type="password" name="pwd" class="form-control" placeholder="비밀번호를 입력해주세요" id="pwd">
			</div>
			
			<div class="form-group">
				<label for="password">비밀번호확인</label> 
				<input type="password" name="pwd2" class="form-control" placeholder="비밀번호를 입력해주세요" id="pwd2">
			</div>
		</c:if>
		
	</form>
	<button id="btn-update" class="btn btn-primary">회원수정완료</button>

</div>

<script src="<c:url value='/js/member.js'/>"></script>

<%@ include file="/WEB-INF/views/include/footer.jsp"%>


