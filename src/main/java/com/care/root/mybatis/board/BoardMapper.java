package com.care.root.mybatis.board;

import java.util.List;


import com.care.root.board.dto.BoardDTO;

public interface BoardMapper {
	public List<BoardDTO> selectAllBoardList();
	public int writeSave(BoardDTO dto);
	public BoardDTO contentView(int writeNo);
	public void upHit(int writeNo);
	public int delete(int writeNo);
}
