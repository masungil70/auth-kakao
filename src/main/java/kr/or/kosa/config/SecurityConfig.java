package kr.or.kosa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.DispatcherType;
import kr.or.kosa.config.auth.AuthFailureHandler;
import kr.or.kosa.config.auth.AuthSucessHandler;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // 시큐리티 필터 등록
// 특정 페이지에 특정 권한이 있는 유저만 접근을 허용할 경우 권한 및 
// 인증을 미리 체크하겠다는 설정을 활성화한다.
@RequiredArgsConstructor
public class SecurityConfig {
	
	//@Autowired
	final private AuthSucessHandler authSucessHandler;
	//@Autowired
	final private AuthFailureHandler authFailureHandler;
	
	// BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화 객체 (BCrypt라는 해시 함수를 이용하여 패스워드를 암호화 한다.)
	// 회원 비밀번호 등록시 해당 메서드를 이용하여 암호화해야 로그인 처리시 동일한 해시로 비교한다.
	// 의존성 주입을 위한 함수를 Bean 객체로 리턴할 수 있게 함수를 구현한다 
	@Bean
	public BCryptPasswordEncoder encryptPassword() {
		return new BCryptPasswordEncoder();
	}
	
	//url 설정 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
        http.csrf(csrf ->csrf.disable())	// csrf 사용불가능 
			.authorizeHttpRequests(matchers -> matchers
				//controller에서 jsp view로 forward 하는 경우는 인증 필요없음					
				.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()   
				.requestMatchers("/", "/auth/**", "/api/auth/**", "/js/**","/css/**","/images/**").permitAll() // 해당 경로들은 접근을 허용
				.requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN") //여러개의 권한 중 하나라도 있으면 성공 
				.requestMatchers("/manager/**").hasAnyAuthority("ROLE_MANAGER","ROLE_ADMIN") //MANAGER, ADMIN 권한만 허가 됨 
				.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN") //ADMIN 권한만 허가  
				.anyRequest() // 다른 모든 요청은
				.authenticated() // 인증된 유저만 접근을 허용
			)
		.formLogin(loginForm -> loginForm // 로그인 폼은
				.loginPage("/auth/loginForm.do") // 해당 주소로 로그인 페이지를 호출한다.
				.loginProcessingUrl("/auth/login.do") // 해당 URL로 요청이 오면 스프링 시큐리티가 가로채서 로그인처리를 한다. -> loadUserByName
				.successHandler(authSucessHandler) // 성공시 요청을 처리할 핸들러
				.failureHandler(authFailureHandler) // 실패시 요청을 처리할 핸들러
			)
		.logout(logout -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout.do")) // 로그아웃 URL
			    .logoutSuccessUrl("/auth/loginForm.do") // 성공시 리턴 URL
			    .invalidateHttpSession(true) // 인증정보를 지우하고 세션을 무효화
			    .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
				.permitAll()
			)
        .sessionManagement(session -> session
            .maximumSessions(1) // 세션 최대 허용 수 1, -1인 경우 무제한 세션 허용
            .maxSessionsPreventsLogin(false) // true면 중복 로그인을 막고, false면 이전 로그인의 세션을 해제
            .expiredUrl("/auth/loginForm.do?error=true&exception=Have been attempted to login from a new place. or session expired")  // 세션이 만료된 경우 이동 할 페이지를 지정
            );
        return http.build();
    }
	
}
