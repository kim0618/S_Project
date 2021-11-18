package com.care.root.board.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.care.root.board.dto.BoardDTO;
import com.care.root.common.MemberSessionName;
import com.care.root.mybatis.board.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired BoardMapper mapper;
	@Autowired BoardFileService bfs;

	public void selectAllboardList(Model model) {
		model.addAttribute("boardList", mapper.selectAllBoardList());

	}

	public String writeSave(MultipartHttpServletRequest mul, 
			HttpServletRequest request) {
		BoardDTO dto = new BoardDTO();
		dto.setTitle( mul.getParameter("title") );
		dto.setContent( mul.getParameter("content") );
		dto.setId(mul.getParameter("id"));
		/*	HttpSession session = request.getSession();				// dto.setId와 같은 거
		dto.setId((String)session.getAttribute(MemberSessionName.LOGIN));	*/

		MultipartFile file = mul.getFile("image_file_name");
		//	BoardFileService bfs = new BoardFileServiceImpl();
		if(file.getSize() != 0) {	// 이미지
			//이미지 있을경우 처리
			dto.setImageFileName(bfs.saveFile(file));
		}else {
			dto.setImageFileName("nan");
		}
		int result = 0;
		try {
			result = mapper.writeSave(dto);	// db에서 문제가 생긴거니까 try로 묶어주면 좋음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bfs.getMessage(result, request);
	}


	public void contentView(int writeNo, Model model) {
		model.addAttribute("personalData", mapper.contentView(writeNo));
		upHit(writeNo);

	}
	public void upHit(int writeNo) {
		mapper.upHit(writeNo);
	}

	public String boardDelete(int writeNo,String imageFileName, 
			HttpServletRequest request) {
		
		int result = mapper.delete(writeNo);
	//	MessageDTO mDTO = new MessageDTO();
		String message = null;
		if(result == 1) {
			bfs.deleteImage(imageFileName);
			message = bfs.getMessage(request, "삭제성공","/board/boardAllList");
		
			
		}else {
			bfs.deleteImage(imageFileName);
			message = bfs.getMessage(request, "삭제실패","/board/contentView");
		}
		
		
		return message;
	}


}
