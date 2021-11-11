package com.care.root.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
							RedirectAttributes rs) {
		int result = ms.userCheck(id, pw);
		
		if(result == 0) {
			rs.addAttribute("id", id);
			return "redirect:successLogin";
		}else {
			return "redirect:login";
		}
	}
	@GetMapping("/successLogin")
	public String successLogin(@RequestParam String id, HttpSession session) {
		session.setAttribute( LOGIN , id);
		return "member/successLogin";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute( LOGIN ) != null)
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








