
$(function(){
	
	$("#btn-save").on("click", () => {  
		alert('Member 의 save함수 호출됨');
/*		
		let param = {
			email: $("#email").val(),
			pwd: $("#pwd").val()
		};
		
		console.log(param);
		$.ajax({ 
			type: "POST",
			url: "/api/auth/save",
			data: JSON.stringify(param), 
			contentType: "application/json; charset=utf-8",
			dataType: "json" // 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면) => javascript오브젝트로 변경
		}).success(function(resp){
			if(resp.status === 500){
				alert("회원가입에 실패하였습니다.");
			}else{
				alert("회원가입이 완료되었습니다.");
				location.href = "/";
			}

		}).error(function(error){
			alert(JSON.stringify(error));
		});
*/
		myFetch({
			 url : "/api/auth/save"
			,data : "mForm"
			,success : resp => {
				console.log(resp)
				if(resp.status === 500) {
					alert("회원가입에 실패하였습니다.");
				} else {
					alert("회원가입이 완료되었습니다.");
					location.href = "/";
				}
			}
			,error : (message) => {
				alert(message);
			}
		});
	});
	
	$("#btn-update").on("click", ()=>{  
		alert('member의 update함수 호출됨');
		let param = {
			email: $("#email").val(),
			pwd: $("#pwd").val()
		};
			
		console.log(param);
		
		$.ajax({ 
			type: "PUT",
			url: "/api/auth/member",
			data: JSON.stringify(param), 
			contentType: "application/json; charset=utf-8",
			dataType: "json" 
		}).done(function(resp){
			if(resp.status === 500){
				alert("회원정보 수정 실패하였습니다.");
			}else{
				alert("회원정보 수정이 완료되었습니다.");
				location.href = "/";
			}

		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	});
})

