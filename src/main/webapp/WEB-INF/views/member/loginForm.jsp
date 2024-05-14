<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<%@ include file="/WEB-INF/views/include/header.jsp"%>

<div class="container">
	<form name="loginForm" id="loginForm" method="post" action="<c:url value='/auth/login.do'/>" >
		${error ? exception : ''}

		<div class="form-group">
			<label for="username">이메일</label> 
			<input type="text" name="username" class="form-control" placeholder="이메일을 입력해주세요" id="username">
		</div>
				
		<div class="form-group">
			<label for="password">비밀번호</label> 
			<input type="password" name="password" class="form-control" placeholder="비밀번호를 입력해주세요" id="password">
		</div>
		
		
		<INPUT id="btn-login" class="btn btn-primary" type="submit" value="로그인"> 
		<INPUT id="btn-login" class="btn btn-primary" type="button" value="초기화">
		
		<a href="https://kauth.kakao.com/oauth/authorize?client_id=3ecdf9fa9af2ebe4cfc27575c1d45f83&redirect_uri=http://localhost:8090/auth/kakao/callback&response_type=code"><img src="/images/kakao_login_medium_narrow.png" /></a>
		
		<br/><br/>
		   <a href="#">아이디 찾기</a>  | 
		   <a href="#">비밀번호 찾기</a> | 
		   <a href="<c:url value='/auth/registerForm.do'/>">회원가입</a>    | 
		   <a href="#">고객 센터</a>
	</form>		
</div>


<%@ include file="/WEB-INF/views/include/footer.jsp"%>