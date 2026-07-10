package io.hexlet.spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 2, max = 255, message = "Title must contain from 2 to 255 characters")
    private String title;

    @NotBlank(message = "Content can't be blank")
    private String content;

    @NotBlank(message = "Author can't be blank")
    private String author;

    private LocalDateTime createdAt;
}
