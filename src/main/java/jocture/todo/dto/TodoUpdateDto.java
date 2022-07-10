package jocture.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoUpdateDto {
    @NotNull
    private Integer id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String title;

    private boolean done;

}
