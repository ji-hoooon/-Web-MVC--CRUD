package org.zerock.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.springex.dto.PageRequestDTO;
import org.zerock.springex.dto.PageResponseDTO;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.service.TodoService;

import java.time.LocalDate;

@Log4j2
@ExtendWith(SpringExtension.class)
//어떤 컨텍스트에서 테스트를 수행할지 결정하는 xml 설정파일 경로 지정 어노테이션
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TodoServiceTests {

    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister(){
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Test...")
                .dueDate(LocalDate.now())
                .writer("user1")
                .build();
        todoService.register(todoDTO);
    }

    @Test
    public void testPaging(){
        //PageResponseDTO의 생성자에 필요한 파라미터 :
        // TodoDTO의 목록, 전체 데이터 수, 페이지 번호의 처리를 위한 데이터들 (시작 페이지 번호 / 끝 페이지 번호)

        //페이지 번호의 처리를 위한 데이터들
        PageRequestDTO pageRequestDTO= PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<TodoDTO> pageResponseDTO = todoService.getList(pageRequestDTO);

        log.info(pageResponseDTO);

        pageResponseDTO.getDtoList().stream().forEach(todoDTO -> log.info(todoDTO));
    }

}
