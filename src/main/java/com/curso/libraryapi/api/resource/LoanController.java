package com.curso.libraryapi.api.resource;

import com.curso.libraryapi.api.dto.LoanDTO;
import com.curso.libraryapi.api.dto.ReturnedLoanDTO;
import com.curso.libraryapi.model.entity.Book;
import com.curso.libraryapi.model.entity.Loan;
import com.curso.libraryapi.service.BookService;
import com.curso.libraryapi.service.LoanService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

  private final LoanService service;
  private final BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody LoanDTO dto) {
    Book book = bookService
      .getBookByIsbn(dto.getIsbn())
      .orElseThrow(
        () ->
          new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Book not found for passed isbn"
          )
      );
    Loan entity = Loan
      .builder()
      .book(book)
      .customer(dto.getCustomer())
      .loanDate(LocalDate.now())
      .build();

    entity = service.save(entity);

    return entity.getId();
  }

  @PatchMapping("{id}")
  public void returnBook(
    @PathVariable Long id,
    @RequestBody ReturnedLoanDTO dto
  ) {
    Loan loan = service
      .getById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    loan.setReturned(dto.getReturned());

    service.update(loan);
  }
}
