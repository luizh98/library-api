package com.curso.libraryapi.service;

import com.curso.libraryapi.model.entity.Loan;
import java.util.Optional;

public interface LoanService {
  Loan save(Loan loan);

  Optional<Loan> getById(Long id);

  Loan update(Loan loan);
}
