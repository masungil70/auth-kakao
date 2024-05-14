package kr.or.kosa.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.kosa.dao.MemberDAO;
import kr.or.kosa.exception.ExistMemberException;
import kr.or.kosa.exception.NotExistMemberException;
import kr.or.kosa.model.MemberVO;

@Service
public class MemberService  {

	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


	public MemberVO findByEmail(String email) {
		return memberDAO.findByEmail(email);
	}
	
	public void insertMember(MemberVO memberVO) throws Exception {
		try {
			if (memberVO == null ||
				Objects.isNull(memberVO.getEmail())) {
				throw new Exception("아이디는 필수 정보입니다");
			} else if (Objects.isNull(memberVO.getPwd())) {
				throw new Exception("비밀번호는 필수 정보입니다");
			}
			MemberVO existMember = memberDAO.findByEmail(memberVO.getEmail());
			if (existMember != null && !Objects.isNull(memberVO.getEmail())) {
				throw new ExistMemberException(memberVO.getEmail());
			}
			//비밀번호 암호화 한다
			memberVO.setPwd(passwordEncoder.encode(memberVO.getPwd()));
			memberDAO.insertMember(memberVO);
			System.out.println(memberVO);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	public boolean updateMember(MemberVO memberVO)  {
		try {
			if (memberVO == null || Objects.isNull(memberVO.getEmail())) {
				throw new Exception("아이디는 필수 정보입니다");
			} else if (Objects.isNull(memberVO.getPwd())) {
				throw new Exception("비밀번호는 필수 정보입니다");
			}
			MemberVO existMember = memberDAO.findByEmail(memberVO.getEmail());
			if (existMember == null || Objects.isNull(memberVO.getEmail())) {
				throw new NotExistMemberException(memberVO.getEmail());
			}
			
			//비밀번호 암호화 한다
			memberVO.setPwd(passwordEncoder.encode(memberVO.getPwd()));
			
			memberDAO.updateMember(memberVO);
			
			memberVO.setRoles(existMember.getRoles());
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}