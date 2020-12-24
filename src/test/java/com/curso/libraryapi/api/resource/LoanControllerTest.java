package com.curso.libraryapi.api.resource;

import com.curso.libraryapi.api.dto.LoanDTO;
import com.curso.libraryapi.api.dto.ReturnedLoanDTO;
import com.curso.libraryapi.exception.BusinessException;
import com.curso.libraryapi.model.entity.Book;
import com.curso.libraryapi.model.entity.Loan;
import com.curso.libraryapi.service.BookService;
import com.curso.libraryapi.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("teste")
@AutoConfigureMockMvc
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

  static final String LOAN_API = "/api/loans";

  @Autowired
  MockMvc mvc;

  @MockBean
  private BookService bookService;

  @MockBean
  private LoanService loanService;

  @Test
  @DisplayName("Deve realizar um emprestimo")
  public void createLoanTest() throws Exception {
    LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    Book book = Book.builder().id(1l).isbn("123").build();
    BDDMockito
      .given(bookService.getBookByIsbn("123"))
      .willReturn(Optional.of(book));

    Loan loan = Loan
      .builder()
      .id(1l)
      .customer("Fulano")
      .book(book)
      .loanDate(LocalDate.now())
      .build();
    BDDMockito
      .given(loanService.save(Mockito.any(Loan.class)))
      .willReturn(loan);

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
      .post(LOAN_API)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(json);

    mvc
      .perform(request)
      .andExpect(MockMvcResultMatchers.status().isCreated())
      .andExpect(MockMvcResultMatchers.content().string("1"));
  }

  @Test
  @DisplayName(
    "Deve retornar erro ao tentar fazer emprestimo de um livro inexistente."
  )
  public void invalidIsbnCreateLoanTeste() throws Exception {
    LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    BDDMockito
      .given(bookService.getBookByIsbn("123"))
      .willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
      .post(LOAN_API)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(json);

    mvc
      .perform(request)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
      .andExpect(
        MockMvcResultMatchers
          .jsonPath("errors[0]")
          .value("Book not found for passed isbn")
      );
  }

  @Test
  @DisplayName(
    "Deve retornar erro ao tentar fazer emprestimo de um livro emprestado."
  )
  public void loanedBookErrorOnCreateLoanTeste() throws Exception {
    LoanDTO dto = LoanDTO.builder().isbn("123").customer("Fulano").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    Book book = Book.builder().id(1l).isbn("123").build();
    BDDMockito
      .given(bookService.getBookByIsbn("123"))
      .willReturn(Optional.of(book));

    BDDMockito
      .given(loanService.save(Mockito.any(Loan.class)))
      .willThrow(new BusinessException("Book already loaned"));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
      .post(LOAN_API)
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .content(json);

    mvc
      .perform(request)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
      .andExpect(
        MockMvcResultMatchers.jsonPath("errors[0]").value("Book already loaned")
      );
  }

  @Test
  @DisplayName("Deve retornar um livro.")
  public void returnBookTest() throws Exception {
    // cenário { returned: true }
    ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
    Loan loan = Loan.builder().id(1l).build();
    BDDMockito
      .given(loanService.getById(Mockito.anyLong()))
      .willReturn(Optional.of(loan));

    String json = new ObjectMapper().writeValueAsString(dto);

    mvc
      .perform(
        MockMvcRequestBuilders
          .patch(LOAN_API.concat("/1"))
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(json)
      )
      .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(loanService, Mockito.times(1)).update(loan);
  }

  @Test
  @DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente.")
  public void returnInexistentBookTest() throws Exception {
    // cenário { returned: true }
    ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
    String json = new ObjectMapper().writeValueAsString(dto);

    BDDMockito
      .given(loanService.getById(Mockito.anyLong()))
      .willReturn(Optional.empty());

    mvc
      .perform(
        MockMvcRequestBuilders
          .patch(LOAN_API.concat("/1"))
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(json)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
