package com.curso.libraryapi.service.impl;

import com.curso.libraryapi.exception.BusinessException;
import com.curso.libraryapi.model.entity.Loan;
import com.curso.libraryapi.model.repository.LoanRepository;
import com.curso.libraryapi.service.LoanService;
import java.util.Optional;

public class LoanServiceImpl implements LoanService {

  private LoanRepository repository;

  public LoanServiceImpl(LoanRepository repository) {
    this.repository = repository;
  }

  @Override
  public Loan save(Loan loan) {
    if (repository.existsByBookAndNotReturned(loan.getBook())) {
      throw new BusinessException("Book already loaned");
    }
    return repository.save(loan);
  }

  @Override
  public Optional<Loan> getById(Long id) {
    return repository.findById(id);
  }

  @Override
  public Loan update(Loan loan) {
    return null;
  }
}
