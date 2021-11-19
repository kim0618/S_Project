package com.care.root.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.care.root.board.service.BoardFileService;
import com.care.root.board.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {
	@Autowired BoardService bs;
	@GetMapping("boardAllList")
	public String selectAllboardList(Model model, @RequestParam(required = false, defaultValue= "1")int num) {
		bs.selectAllboardList(model, num);

		return "board/boardAllList";
	}

	@GetMapping("writeForm")
	public String writeForm() {
		return "board/writeForm";
	}

	@PostMapping("writeSave")
	public void writeSave(MultipartHttpServletRequest mul,
			HttpServletResponse response,
			HttpServletRequest request) throws IOException {

		String message = bs.writeSave(mul, request);
		PrintWriter out=null;
		response.setContentType("text/html; charset=utf-8");
		out = response.getWriter();
		out.println(message);
	}

	@GetMapping("contentView")
	public String contentView(@RequestParam int writeNo, Model model) {
		bs.contentView(writeNo, model);
		return "board/contentView";
	}

	@GetMapping("download")
	public void download(@RequestParam("imageFileName") String imageFileName,		// 이미지 보이게 하기 위해 쓰는 거
	         HttpServletResponse response) throws IOException {
	    response.addHeader(
	   "Content-disposition","attachment;fileName="+imageFileName);
	    File file = new File(BoardFileService.IMAGE_REPO+"/"+imageFileName);
	    FileInputStream in = new FileInputStream(file);
	    FileCopyUtils.copy(in, response.getOutputStream());
	    in.close();
	}

	@GetMapping("delete")	//삭제
	public void boardDelete(@RequestParam("writeNo") int write_no,
	  @RequestParam("imageFileName") String imageFileName,
	  HttpServletResponse response, 
	  HttpServletRequest request) throws IOException {
	  String message = bs.boardDelete(write_no,imageFileName,request);
	  PrintWriter out=null;
	  response.setContentType("text/html; charset=utf-8");
	  out = response.getWriter();
	  out.println(message);
	}

		
	@GetMapping("modify_form")	// 수정하기
	public String modify_form(@RequestParam int writeNo, Model model) {
		bs.getData(writeNo, model);
		return "board/modify_form";
	}

	@PostMapping("modify")
	public void modify(MultipartHttpServletRequest mul,
	    HttpServletResponse response,
	  HttpServletRequest request) throws IOException {
	  String message = bs.modify(mul, request);
	  PrintWriter out=null;
	  response.setContentType("text/html; charset=utf-8");
	  out = response.getWriter();
	  out.println(message);
	}



}
/*
 * 게시판 db 생성
create table mvc_board(
Write_no number(10) primary key,
Title varchar2(100),
Content varchar2(300),
Savedate date default sysdate,
Hit number(10) default 0,
Image_file_name varchar(100),
Id varchar(20) not null,
constraint fk_test foreign key(id) references membership(id) on delete cascade
);
create sequence mvc_board_seq;
 */
