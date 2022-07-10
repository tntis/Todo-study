package jocture.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jocture.todo.dto.TodoCreateDto;
import jocture.todo.dto.TodoDeleteDto;
import jocture.todo.dto.TodoUpdateDto;
import jocture.todo.mapper.TodoMapper;
import jocture.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest({TodoController.class, TodoMapper.class})
class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TodoMapper todoMapper;
    @MockBean
    TodoService todoService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createTodo() throws Exception {
        //given
        String url = "/todo";
        TodoCreateDto dto = new TodoCreateDto("공부하기");
        String body = objectMapper.writeValueAsString(dto);// Serialize : 객체 -> JSON 스트링으로 변환

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void createTodo_ValidError(String title) throws Exception {
        //given
        String url = "/todo";
        TodoCreateDto dto = new TodoCreateDto(title);
        String body = objectMapper.writeValueAsString(dto);// Serialize : 객체 -> JSON 스트링으로 변환

        // when & then
        mvc.perform(post(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTodo() throws Exception {
        //given
        String url = "/todo";
        TodoUpdateDto dto = new TodoUpdateDto(1, "공부하기", true);
        String body = objectMapper.writeValueAsString(dto);// Serialize : 객체 -> JSON 스트링으로 변환

        // when & then
        mvc.perform(put(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource(value = {":자바:true", "1::false", "::false"}, delimiter = ':')
    void updateTodo_ValidError(Integer id, String title, Boolean done) throws Exception {
        //given
        String url = "/todo";
        TodoUpdateDto dto = new TodoUpdateDto(id, title, done);
        String body = objectMapper.writeValueAsString(dto);// Serialize : 객체 -> JSON 스트링으로 변환

        // when & then
        mvc.perform(put(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTodo() throws Exception {
        //given
        String url = "/todo";
        TodoDeleteDto dto = new TodoDeleteDto(1);
        String body = objectMapper.writeValueAsString(dto);// Serialize : 객체 -> JSON 스트링으로 변환

        // when & then
        mvc.perform(delete(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @NullSource
    void deleteTodo_ValidError(Integer id) throws Exception {
        //given
        String url = "/todo";
        TodoDeleteDto dto = new TodoDeleteDto(id);
        String body = objectMapper.writeValueAsString(dto);// Serialize : 객체 -> JSON 스트링으로 변환

        // when & then
        mvc.perform(put(url).content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}