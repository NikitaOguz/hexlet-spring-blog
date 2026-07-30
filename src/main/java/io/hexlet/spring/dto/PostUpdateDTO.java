package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    private String content;
}
