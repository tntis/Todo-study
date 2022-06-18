package jocture.todo.mapper;

import jocture.todo.config.MapStructConfig;
import jocture.todo.dto.UserDto;
import jocture.todo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "memberName", source = "name")
    User toEntity(UserDto dto);

    UserDto toDto(User user);
}
