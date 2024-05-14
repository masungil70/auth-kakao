package kr.or.kosa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO  {
	
	private Long id;
	private String email;
	private String pwd;
	private String oauth;
	private String roles;
	private String accountExpired; 
	private String accountLocked;
	private int loginCount;
	private LocalDateTime lastLoginTime;
	
	
	
	// ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱!!
	// 예제 ROLL 값 : "ROLE_USER","ROLE_MANAGER","ROLE_ADMIN"  
	public List<String> getRoleList() {
	    if (this.roles.length() > 0) {
	        return Arrays.asList(this.roles.split(","));
	    }
	    return new ArrayList<>();
	}

}



