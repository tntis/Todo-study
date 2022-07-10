package jocture.todo.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import jocture.todo.dto.TodoCreateDto;
import jocture.todo.dto.TodoDeleteDto;
import jocture.todo.dto.TodoDto;
import jocture.todo.dto.TodoUpdateDto;
import jocture.todo.entity.Todo;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-07-10T21:38:40+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 11.0.14 (Amazon.com Inc.)"
)
@Component
public class TodoMapperImpl implements TodoMapper {

    @Override
    public Todo toEntity(TodoDto dto) {
        if ( dto == null ) {
            return null;
        }

        Todo.TodoBuilder todo = Todo.builder();

        todo.id( dto.getId() );
        todo.title( dto.getTitle() );
        todo.done( dto.isDone() );

        return todo.build();
    }

    @Override
    public Todo toEntity(TodoCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        Todo.TodoBuilder todo = Todo.builder();

        todo.title( dto.getTitle() );

        return todo.build();
    }

    @Override
    public Todo toEntity(TodoUpdateDto dto) {
        if ( dto == null ) {
            return null;
        }

        Todo.TodoBuilder todo = Todo.builder();

        todo.id( dto.getId() );
        todo.title( dto.getTitle() );
        todo.done( dto.isDone() );

        return todo.build();
    }

    @Override
    public Todo toEntity(TodoDeleteDto dto) {
        if ( dto == null ) {
            return null;
        }

        Todo.TodoBuilder todo = Todo.builder();

        todo.id( dto.getId() );

        return todo.build();
    }

    @Override
    public TodoDto toDto(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoDto.TodoDtoBuilder todoDto = TodoDto.builder();

        todoDto.id( todo.getId() );
        todoDto.title( todo.getTitle() );
        todoDto.done( todo.isDone() );

        return todoDto.build();
    }

    @Override
    public List<TodoDto> toDtoList(List<Todo> todoList) {
        if ( todoList == null ) {
            return null;
        }

        List<TodoDto> list = new ArrayList<TodoDto>( todoList.size() );
        for ( Todo todo : todoList ) {
            list.add( toDto( todo ) );
        }

        return list;
    }
}
