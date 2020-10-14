package com.curso.libraryapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.curso.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
  @Autowired
  TestEntityManager entityManager;

  @Autowired
  BookRepository repository;

  @Test
  @DisplayName(
    "Deve retornar verdade quando existir um livro na base com o isbn informado"
  )
  public void returnTrueWhenIsbnExists() {
    // cenario
    String isbn = "123";
    Book book = Book
      .builder()
      .title("Aventuras")
      .author("Fulano")
      .isbn(isbn)
      .build();
    entityManager.persist(book);

    // execucao
    boolean exists = repository.existsByIsbn(isbn);

    // verificacao
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName(
    "Deve retornar falso quando não existir um livro na base com o isbn informado"
  )
  public void returnFalseWhenIsbnDoesntExists() {
    // cenario
    String isbn = "123";
   
    // execucao
    boolean exists = repository.existsByIsbn(isbn);

    // verificacao
    assertThat(exists).isFalse();
  }
}
