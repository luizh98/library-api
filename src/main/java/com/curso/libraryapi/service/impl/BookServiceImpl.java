package com.curso.libraryapi.service.impl;

import com.curso.libraryapi.exception.BusinessException;
import com.curso.libraryapi.model.entity.Book;
import com.curso.libraryapi.model.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl
  implements com.curso.libraryapi.service.BookService {
  private BookRepository repository;

  public BookServiceImpl(BookRepository repository) {
    this.repository = repository;
  }

  @Override
  public Book save(Book book) {
    if (repository.existsByIsbn(book.getIsbn())) {
      throw new BusinessException("Isbn já cadastrado.");
    }
    return repository.save(book);
  }
}
