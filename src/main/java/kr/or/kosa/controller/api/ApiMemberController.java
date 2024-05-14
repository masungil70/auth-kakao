package kr.or.kosa.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.or.kosa.config.auth.PrincipalDetails;
import kr.or.kosa.dto.ResponseDTO;
import kr.or.kosa.model.MemberVO;
import kr.or.kosa.service.MemberService;

@RestController
public class ApiMemberController {
	
	@Autowired
	MemberService memberService;
	
	@PostMapping("/api/auth/save")
	public ResponseEntity<ResponseDTO> save(@RequestBody MemberVO memberVO) { // email, pwd
		System.out.println("ApiMemberController : save 호출 => " + memberVO);

		ResponseDTO responseDTO = null;
		
		try {
			memberService.insertMember(memberVO);
			
			responseDTO = ResponseDTO.builder()
					.httpStatus(HttpStatus.OK)
					.message("회원가입 성공")
					.build();
					
		} catch (Exception e) {
			responseDTO = ResponseDTO.builder()
					.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
					.message(e.getMessage())
					.build();
		}
		
		System.out.println(responseDTO);
		
		return ResponseEntity.status(responseDTO.getHttpStatus()).body(responseDTO);
	}

	@PutMapping("/api/auth/member")
	public ResponseEntity<ResponseDTO>  update(@RequestBody MemberVO memberVO) { 
		System.out.println("ApiMemberController : update 호출됨 = " + memberVO);

		ResponseDTO responseDTO = null;
		
		try {
			//회원 정보 수정 
			memberService.updateMember(memberVO);
			
			// 여기서는 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됐음.
			// 하지만 세션값은 변경되지 않은 상태이기 때문에 직접 세션값을 변경해줄 것임.
			// 세션 등록
			// 로그인 처리 
			PrincipalDetails principalDetails = new PrincipalDetails(memberVO);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
					principalDetails.getPassword(), //   
					principalDetails.getAuthorities()); //사용자가 소유한 역할 권한을 전달한다 

			// 강제로 시큐리티의 세션에 접근하여 값 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			responseDTO = ResponseDTO.builder()
					.httpStatus(HttpStatus.OK)
					.message("회원 정보 수정 성공")
					.build();
					
		} catch (Exception e) {
			responseDTO = ResponseDTO.builder()
					.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
					.message(e.getMessage())
					.build();
		}
		
		return ResponseEntity.status(responseDTO.getHttpStatus()).body(responseDTO);
	}
	
}