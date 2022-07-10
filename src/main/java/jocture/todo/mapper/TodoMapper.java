package jocture.todo.mapper;

import jocture.todo.config.MapStructConfig;
import jocture.todo.dto.TodoCreateDto;
import jocture.todo.dto.TodoDeleteDto;
import jocture.todo.dto.TodoDto;
import jocture.todo.dto.TodoUpdateDto;
import jocture.todo.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface TodoMapper {

    Todo toEntity(TodoDto dto);

    Todo toEntity(TodoCreateDto dto);

    Todo toEntity(TodoUpdateDto dto);

    Todo toEntity(TodoDeleteDto dto);

    TodoDto toDto(Todo todo);

    List<TodoDto> toDtoList(List<Todo> todoList);
}
