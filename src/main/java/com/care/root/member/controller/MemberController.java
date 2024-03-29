package com.care.root.member.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.care.root.common.MemberSessionName;
import com.care.root.member.dto.MemberDTO;
import com.care.root.member.service.MemberService;

@Controller
@RequestMapping("member")
public class MemberController implements MemberSessionName{
	@Autowired MemberService ms;
	@GetMapping("login")
	public String login() {
		System.out.println("멤버 로그인 연결");
		return "member/login";
	}
	@GetMapping("main")
	public String main() {
		System.out.println("멤버 로그인 연결111");
		return "default/main";
	}
	@PostMapping("/user_check")
	public String userCheck(@RequestParam String id,
							@RequestParam String pw,
							@RequestParam (required = false)String autoLogin,	// autoLogin 체크면 null 체크하면 값
							RedirectAttributes rs) {
		int result = ms.userCheck(id, pw);
		System.out.println("autoLogin : "+autoLogin);
		if(result == 0) {
			rs.addAttribute("id", id);
			rs.addAttribute("autoLogin",autoLogin);
			return "redirect:successLogin";
		}else {
			return "redirect:login";
		}
	}
	@GetMapping("/successLogin")
	public String successLogin(@RequestParam String id, HttpSession session,
								@RequestParam (required = false)String autoLogin,
								HttpServletResponse response) {
		System.out.println("id : "+id);
		System.out.println("autoLogin : "+autoLogin);
		session.setAttribute( LOGIN , id);
		if(autoLogin != null) {
			int limitTime = 60*60*24*90; // 90일
			Cookie loginCookie = new Cookie("loginCookie", session.getId());
			loginCookie.setPath("/");
			loginCookie.setMaxAge(limitTime);
			response.addCookie(loginCookie);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());	// 현재 시간을 얻어오기
			cal.add(Calendar.MONTH, 3);	// 3개월 후
			
			java.sql.Date limitDate = new java.sql.Date(cal.getTimeInMillis());	// 3개월 시간 설정 sql시간으로 변환
			ms.keepLogin(session.getId(), limitDate, id);
			
		}
		return "member/successLogin";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session, 
					HttpServletResponse response, 
					@CookieValue(value="loginCookie", required = false) Cookie loginCookie) {
		if(session.getAttribute( LOGIN ) != null) {
			if(loginCookie != null) {
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				ms.keepLogin("nan",
						new java.sql.Date(System.currentTimeMillis()),
						(String)session.getAttribute(LOGIN));
			}
		}
			session.invalidate();
		return "redirect:main";
	}
	@GetMapping("memberInfo")
	public String memberInfo(Model model, HttpSession session) {
	//	if(session.getAttribute(LOGIN) != null) {
			ms.memberInfo(model);
			return "member/memberInfo";
	//	}
//		return "redirect:login";
	}
	@GetMapping("info")
	public String info(@RequestParam String id, Model model) {
		ms.info(model, id);
		return "member/info";
	}
	@GetMapping("register_form")
	public String registerForm() {
		return "member/register";
	}
	@PostMapping("register")
	public String register(MemberDTO dto) {
		int result = ms.register(dto);
		if(result == 1)
			return "redirect:login";
		return "redirect:register_form";
	}
}








