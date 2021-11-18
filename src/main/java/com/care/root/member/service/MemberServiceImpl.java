package com.care.root.member.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.care.root.member.dto.MemberDTO;
import com.care.root.mybatis.member.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired MemberMapper mapper;
	BCryptPasswordEncoder encoder;	// 우리가 사용하는 값을 암호화하는 것
	
	public MemberServiceImpl() {
		encoder = new BCryptPasswordEncoder();
	}
	
	public int userCheck(String id,String pw) {
		MemberDTO dto = mapper.userCheck(id);
		if(dto != null) {
		//	if(pw.equals(dto.getPw())) {
			if(encoder.matches(pw, dto.getPw()) || pw.equals(dto.getPw())) {	// 암호화한거 비교
				return 0;
			}
		}
		return 1;
	}
	
	public void memberInfo(Model model) {
		model.addAttribute("memberList", mapper.memberInfo() );
	}
	
	public void info(Model model, String id) {
		model.addAttribute( "info",mapper.userCheck(id) );
	}
	
	public int register(MemberDTO dto) {
		System.out.println("비번 변경 전 : "+dto.getPw());
		String securePw = encoder.encode(dto.getPw());		// 가입할때 비밀번호 암호화해서 넣기
		System.out.println("비번 변경 후 : "+dto.getPw());
		
		dto.setPw(securePw);
		
		int result = 0;
		try {
			result = mapper.register(dto);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void keepLogin(String sessionId, Date limitDate, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sessionId", sessionId);
		map.put("limitDate", limitDate);
		map.put("id", id);
		mapper.keepLogin(map);
		
	}

	@Override
	public MemberDTO getUserSessionId(String sessionId) {
		
		return mapper.getUserSessionId(sessionId);
	}
}








