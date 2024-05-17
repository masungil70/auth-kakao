package kr.or.kosa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.kosa.config.auth.PrincipalDetails;
import kr.or.kosa.exception.ExistMemberException;
import kr.or.kosa.model.KakaoProfile;
import kr.or.kosa.model.MemberVO;
import kr.or.kosa.model.OAuthToken;
import kr.or.kosa.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	//@Autowired
	final MemberService memberService;
	
	@GetMapping("/")
	public String main(Model model) {
		return "index";
	}

	//로그인 양식 
	@GetMapping("/auth/loginForm.do")
	public String loginForm(Model model,
			@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "exception", required = false) String exception) {
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		return "member/loginForm";
	}

	//회원 가입 양식 
	@GetMapping("/auth/joinForm.do")
	public String joinForm() {
		return "member/joinForm";
	}
	
	//회원 정보 수정 양식
	@GetMapping("/user/updateForm.do")
	public String updateForm() {	
		return "member/updateForm";
	}
	
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수
		
		// POST방식으로 key=value 데이터를 요청 (카카오쪽으로)
		// Retrofit2
		// OkHttp
		// RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "3ecdf9fa9af2ebe4cfc27575c1d45f83");
		params.add("redirect_uri", "http://localhost:8090/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response = restTemplate.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		//응답 결과 body 출력
		System.out.println("https://kauth.kakao.com/oauth/token의 body = " + response.getBody());
		
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰 : "+oauthToken.getAccess_token());
		
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);
		
		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
		);
		System.out.println(response2.getBody());
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// User 오브젝트 : username, password, email
		//System.out.println("카카오 아이디(번호) : "+kakaoProfile.getId());
		System.out.println("카카오 이메일 : "+kakaoProfile.getKakao_account().getEmail());
		
		MemberVO kakaoMember = MemberVO.builder()
				.email(kakaoProfile.getKakao_account().getEmail())
				.roles("USER")
				.oauth("kakao")
				.build();
		
		// 가입자 혹은 비가입자 체크 해서 처리
		try {
			memberService.insertMember(kakaoMember);
			System.out.println("기존 회원이 아니기에 자동 회원가입을 진행함");
		} catch (ExistMemberException e) {
			System.out.println("기존에 회원 가입된 경우 다음으로 진행함");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("자동 로그인을 진행합니다.");

		// 로그인 처리 
		PrincipalDetails principalDetails = new PrincipalDetails(kakaoMember);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
				null, // 토큰 인증시 패스워드는 알수 없어 null 값을 전달하는 것임  
				principalDetails.getAuthorities()); //사용자가 소유한 역할 권한을 전달한다 

		// 강제로 시큐리티의 세션에 접근하여 값 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		
		return "redirect:/";
	}
	
	
}