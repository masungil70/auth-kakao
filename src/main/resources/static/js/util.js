/**
 * 
 */
//formId : 폼의 아이디  
//data : reduce() 함수에 의해 리턴되는 객체 
//element : reduce() 함수에 의해 호출 되는 배열 요소 값
//elements :  배열 객체 
//formToSerialize은 함수의 선언한 것임 
//formToSerialize() 함수는 form에 있는 모든 입력 요소의 값을 JSON 객체 형태의 문자열로 리턴하는 함수 임 
//함수를 fetch() 함수의 body 로 전달 할 때 사용하면 됨 
//반드시 기억할 것은 위 함수는 일반적으로 사용하는 것이 아니라 내가 교육중 작성한 것이라는 것을 기억 할 것 
//회사에서는 이와 비슷한 함수가 있을 것임 

 const formToSerialize = (formId) => JSON.stringify([].reduce.call(document.querySelector('#' + formId), (data, element) => {
    //이름이 있는 것을 대상으로함 
    if (element.name == '') return data;
    //radio와 checkbox인 경우는 반드시 선택된 것만 대상으로함 
     if (element.type == 'radio') {
        if (element.checked) {
         	data[element.name] = element.value;
        }
	 } else if (element.type == 'checkbox') {
        if (element.checked) {
			const value = data[element.name];
			const valueType = typeof(value);
			if (valueType == 'undefined') {
	         	data[element.name] = element.value;
			}  else if (valueType == 'string' || valueType == 'number') {
	         	data[element.name] = [...value, element.value];
			}  else if (typeof(value) != 'undefined') {
	         	data[element.name] = [...value, element.value];
			}
        }
     } else {
        //그외는 모두 대상으로 함 
        data[element.name] = element.value;
     }
     return data;
    },
    {} //초기값 
 )
);

/*
url : 실행할 서버의 주소 (필수) 
data : form의 아이디 또는 전송할 데이터로 연관 배열 형태 (필수)
success : 성공시 처리할 함수 (필수)
error : 실패시 처리할 함수(옵션) 
 */
const myFetch = (args) => {
	let body = typeof (args.data) == "string" ? formToSerialize(args.data) : JSON.stringify(args.data);
/*	     
	fetch(args.url, {
		method:"post",
		headers: {
			"Content-type" : "application/json; charset=utf-8"
		},
		body : body
	})
	.then(response => response.json())
	.then(json => {
		success(json); //성공시 처리 루틴으로 이동 
	})
	.catch(err => {
		if (args.error != null) {
			args.error (typeof (error.responseJSON) == 'object' ? error.responseJSON.message : JSON.stringify(error) );
		}
	}); //실패시 처리 루틴으로 이동
*/	
	$.ajax({ 
		type: "POST",
		url: args.url,
		data: body, 
		contentType: "application/json; charset=utf-8",
		dataType: "json" // 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면) => javascript오브젝트로 변경
	}).done(resp => {
		args.success(resp);
	}).fail(err => {
		if (args.error != null) {
			args.error (typeof (err.responseJSON) == 'object' ? err.responseJSON.message : JSON.stringify(err) );
		}
	}); 
		 
};
