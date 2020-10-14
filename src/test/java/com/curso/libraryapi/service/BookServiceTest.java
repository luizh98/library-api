package com.curso.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.curso.libraryapi.exception.BusinessException;
import com.curso.libraryapi.model.entity.Book;
import com.curso.libraryapi.model.repository.BookRepository;
import com.curso.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
  BookService service;

  @MockBean
  BookRepository repository;

  @BeforeEach
  public void setUp() {
    this.service = new BookServiceImpl(repository);
  }

  @Test
  @DisplayName("Deve salvar um livro")
  public void saveBookTest() {
    // cenário
    Book book = createValidBook();
    Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);

    Mockito
      .when(repository.save(book))
      .thenReturn(
        Book
          .builder()
          .id((long) 11)
          .isbn("123")
          .author("Fulano")
          .title("As aventuras")
          .build()
      );

    // execução
    Book savedBook = service.save(book);

    // verificação
    assertThat(savedBook.getId()).isNotNull();
    assertThat(savedBook.getIsbn()).isEqualTo("123");
    assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
    assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
  }

  @Test
  @DisplayName(
    "Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado"
  )
  public void shouldNotSaveABookWithDuplicatedISBN() {
    // cenario
    Book book = createValidBook();
    Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

    // execucao
    Throwable exception = Assertions.catchThrowable(() -> service.save(book));

    // verificacoes
    assertThat(exception)
      .isInstanceOf(BusinessException.class)
      .hasMessage("Isbn já cadastrado.");

    Mockito.verify(repository, Mockito.never()).save(book);
  }

  private Book createValidBook() {
    return Book
      .builder()
      .isbn("123")
      .author("Fulano")
      .title("As aventuras")
      .build();
  }
}
