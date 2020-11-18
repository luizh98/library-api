package com.curso.libraryapi.api.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
  
  @ApiModelProperty(value = "Identificador do livro")
  private Long id;

  @NotEmpty
  private String title;

  @NotEmpty
  private String author;

  @NotEmpty
  private String isbn;
}
