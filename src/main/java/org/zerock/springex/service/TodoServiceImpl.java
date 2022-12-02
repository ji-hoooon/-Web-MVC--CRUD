package org.zerock.springex.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.springex.domain.TodoVO;
import org.zerock.springex.dto.PageRequestDTO;
import org.zerock.springex.dto.PageResponseDTO;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.mapper.TodoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{
    private final TodoMapper todoMapper;
    private final ModelMapper modelMapper;

    @Override
    public void register(TodoDTO todoDTO) {
        log.info("register");
        log.info(modelMapper);
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        log.info(todoVO);
        todoMapper.insert(todoVO);
    }

    @Override
    public List<TodoDTO> getAll() {
        List<TodoDTO> dtoList = todoMapper.selectAll().stream()
                .map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public TodoDTO getOne(Long tno) {
        TodoVO vo=todoMapper.selectOne(tno);
        TodoDTO dto = modelMapper.map(vo, TodoDTO.class);

        return dto;
    }

    @Override
    public void remove(Long tno) {
        todoMapper.delete(tno);
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
        todoMapper.update(todoVO);
    }

    @Override
    public PageResponseDTO getList(PageRequestDTO pageRequestDTO) {
        //PageRequestDTO를 통해 PageResponseDTO 생성자를 이용해 객체 생성해야하므로
        //PageResponseDTO의 생성자에 필요한 파라미터 :
        // TodoDTO의 목록, 전체 데이터 수, 페이지 번호의 처리를 위한 데이터들 (시작 페이지 번호 / 끝 페이지 번호)

        //1. List<TodoVO> -> List<TodoDTO>
        List<TodoVO> voList=todoMapper.selectList(pageRequestDTO);
        List<TodoDTO> dtoList=voList.stream()
                .map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());

        //2. total
        int total = todoMapper.getCount(pageRequestDTO);

        //생성자를 이용해 객체 생성
        PageResponseDTO<TodoDTO> pageResponseDTO=
                PageResponseDTO.<TodoDTO>withAll()
                        .dtoList(dtoList)
                        .total(total)
                        //3. 페이지 번호의 처리를 위한 데이터들
                        .pageRequestDTO(pageRequestDTO)
                        .build();

        return pageResponseDTO;
    }


}
