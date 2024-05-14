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
     if (element.type == 'radio' || element.type == 'checkbox') {
        if (element.checked) {
            data[element.name] = element.value;
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

const skFetch = (param, url, handler) => {
	let body = typeof (param) == "string" ? formToSerialize(param) : JSON.stringify(param);     
	fetch(url, {
		method:"post",
		headers: {
			"Content-type" : "application/json; charset=utf-8"
		},
		body : body
	})
	.then(response => response.json())
	.then(resultJson => {
		handler(resultJson);
	});
};
