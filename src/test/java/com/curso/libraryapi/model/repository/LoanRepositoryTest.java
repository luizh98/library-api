package com.curso.libraryapi.model.repository;

import static com.curso.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

import com.curso.libraryapi.model.entity.Book;
import com.curso.libraryapi.model.entity.Loan;
import com.curso.libraryapi.service.LoanService;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

  @Autowired
  private LoanRepository repository;

  @MockBean
  private LoanService loanService;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
  public void existsByBookAndNotReturned() {
    // cenario
    Book book = createNewBook("123");
    entityManager.persist(book);

    Loan loan = Loan
      .builder()
      .book(book)
      .customer("Fulano")
      .loanDate(LocalDate.now())
      .build();
    entityManager.persist(loan);

    // execucao
    boolean exists = repository.existsByBookAndNotReturned(book);

    assertThat(exists).isTrue();
  }
}
