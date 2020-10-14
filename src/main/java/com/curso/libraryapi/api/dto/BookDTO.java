package com.curso.libraryapi.api.dto;

import javax.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
  private Long id;

  @NotEmpty
  private String title;

  @NotEmpty
  private String author;

  @NotEmpty
  private String isbn;
}
